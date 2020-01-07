provider "aws" {
  region = "us-east-1"
}

module "db" {
  source = "./db"
}

data "aws_route53_zone" "default" {
  name = "codemash-observability-workshop.com."
}

data "aws_ami" "amazon-linux-2" {
  most_recent = true

  filter {
    name   = "owner-alias"
    values = ["amazon"]
  }

  filter {
    name   = "name"
    values = ["amzn2-ami-ecs-hvm*-x86_64-ebs"]
  }

  owners = ["amazon"]
}

resource "aws_default_vpc" "default" {}

data "aws_subnet_ids" "default" {
  vpc_id = aws_default_vpc.default.id
}

resource "aws_security_group" "http_ingress" {
  name = "lb-ingress"
  ingress {
    from_port   = 80
    protocol    = "tcp"
    to_port     = 80
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    from_port   = 0
    protocol    = "tcp"
    to_port     = 65535
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_iam_role" "default" {
  name = "codemash-instance-role"

  assume_role_policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Action": "sts:AssumeRole",
            "Principal": {
               "Service": "ec2.amazonaws.com"
            },
            "Effect": "Allow",
            "Sid": ""
        }
    ]
}
EOF
}

resource "aws_iam_instance_profile" "default" {
  name = "codemash-instance"
  role = aws_iam_role.default.name
}

resource "aws_iam_role_policy_attachment" "ssm" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore"
  role = aws_iam_role.default.name
}

resource "aws_iam_role_policy_attachment" "ecs" {
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonEC2ContainerServiceforEC2Role"
  role = aws_iam_role.default.name
}

data "aws_iam_policy_document" "ec2-describe-instances" {
  statement {
    resources = ["*"]
    actions = ["ec2:DescribeInstances"]
  }
}

resource "aws_iam_role_policy" "ec2-describe-instances" {
  policy = data.aws_iam_policy_document.ec2-describe-instances.json
  role = aws_iam_role.default.name
}


resource "aws_ecs_cluster" "default" {
  name = "codemash"
}
