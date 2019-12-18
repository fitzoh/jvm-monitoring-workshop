data "template_file" "loki_user_data" {
  template = file("user-data/user-data.sh")
  vars = {
    cluster  = aws_ecs_cluster.default.name
    workload = "loki"
  }
}

resource "aws_security_group" "loki" {
  name = "loki"
  ingress {
    from_port   = 3100
    protocol    = "tcp"
    to_port     = 3100
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_instance" "loki" {
  ami                         = data.aws_ami.amazon-linux-2.id
  instance_type               = "t3a.small"
  associate_public_ip_address = true
  security_groups             = [aws_security_group.loki.name]
  user_data                   = data.template_file.loki_user_data.rendered
  iam_instance_profile        = aws_iam_instance_profile.default.name
  key_name                    = "fitz-personal-laptop"
  tags = {
    Name = "loki"
  }
  lifecycle { create_before_destroy = true }
}

resource "aws_ecs_service" "loki" {
  name            = "loki"
  cluster         = aws_ecs_cluster.default.id
  task_definition = aws_ecs_task_definition.loki.arn
  desired_count   = 1
  placement_constraints {
    type       = "memberOf"
    expression = "attribute:workload==loki"
  }
}

resource "aws_ecs_task_definition" "loki" {
  family                = "loki"
  network_mode          = "host"
  container_definitions = file("task-definitions/loki.json")
}

resource "aws_route53_record" "loki" {
  name    = "loki.${data.aws_route53_zone.default.name}"
  type    = "A"
  ttl     = "60"
  zone_id = data.aws_route53_zone.default.zone_id
  records = [aws_instance.loki.public_ip]
}
