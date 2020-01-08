data "template_file" "grafana_user_data" {
  template = file("user-data/user-data.sh")
  vars = {
    cluster  = aws_ecs_cluster.default.name
    workload = "grafana"
  }
}

data "template_file" "grafana_task_definition" {
  template = file("task-definitions/grafana.json")
  vars = {
    db_endpoint = module.db.db_endpoint
  }
}



resource "aws_security_group" "grafana" {
  name = "grafana"
  ingress {
    from_port   = 3000
    protocol    = "tcp"
    to_port     = 3000
    cidr_blocks = ["0.0.0.0/0"]
  }
  //node exporter
  ingress {
    from_port   = 9100
    protocol    = "tcp"
    to_port     = 9100
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_instance" "grafana" {
  ami                         = data.aws_ami.amazon-linux-2.id
  instance_type               = "t3a.small"
  associate_public_ip_address = true
  security_groups             = [aws_security_group.grafana.name]
  user_data                   = data.template_file.grafana_user_data.rendered
  iam_instance_profile        = aws_iam_instance_profile.default.name
  key_name                    = "fitz-personal-laptop"
  tags = {
    Name     = "grafana"
    Workload = "grafana"
  }
  lifecycle { create_before_destroy = true }
}

resource "aws_ecs_service" "grafana" {
  name            = "grafana"
  cluster         = aws_ecs_cluster.default.id
  task_definition = aws_ecs_task_definition.grafana.arn
  desired_count   = 1
  placement_constraints {
    type       = "memberOf"
    expression = "attribute:workload==grafana"
  }
}

resource "aws_ecs_task_definition" "grafana" {
  family                = "grafana"
  network_mode          = "host"
  container_definitions = data.template_file.grafana_task_definition.rendered
}

resource "aws_route53_record" "grafana" {
  name    = "grafana.${data.aws_route53_zone.default.name}"
  type    = "A"
  ttl     = "60"
  zone_id = data.aws_route53_zone.default.zone_id
  records = [aws_instance.grafana.public_ip]
}
