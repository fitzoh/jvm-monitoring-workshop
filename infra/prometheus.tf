data "template_file" "prometheus_user_data" {
  template = file("user-data/user-data.sh")
  vars = {
    cluster  = aws_ecs_cluster.default.name
    workload = "prometheus"
  }
}

resource "aws_security_group" "prometheus" {
  name = "prometheus"
  ingress {
    from_port   = 9090
    protocol    = "tcp"
    to_port     = 9090
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_instance" "prometheus" {
  ami                         = data.aws_ami.amazon-linux-2.id
  instance_type               = "t3a.small"
  associate_public_ip_address = true
  security_groups             = [aws_security_group.prometheus.name]
  user_data                   = data.template_file.prometheus_user_data.rendered
  iam_instance_profile        = aws_iam_instance_profile.default.name
  key_name                    = "fitz-personal-laptop"
  tags = {
    Name = "prometheus"
  }
  lifecycle { create_before_destroy = true }
}

resource "aws_ecs_service" "prometheus" {
  name            = "prometheus"
  cluster         = aws_ecs_cluster.default.id
  task_definition = aws_ecs_task_definition.prometheus.id
  desired_count   = 1
  placement_constraints {
    type       = "memberOf"
    expression = "attribute:workload==prometheus"
  }
  lifecycle {
    ignore_changes = ["task_definition"]
  }
}

resource "aws_ecs_task_definition" "prometheus" {
  family                = "prometheus"
  network_mode          = "host"
  container_definitions = file("task-definitions/prometheus.json")
}

resource "aws_route53_record" "prometheus" {
  name    = "prometheus.${data.aws_route53_zone.default.name}"
  type    = "A"
  ttl     = "60"
  zone_id = data.aws_route53_zone.default.zone_id
  records = [aws_instance.prometheus.public_ip]
}
