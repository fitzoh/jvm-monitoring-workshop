data "template_file" "app_user_data" {
  template = file("user-data/user-data.sh")
  vars = {
    cluster  = aws_ecs_cluster.default.name
    workload = "app"
  }
}

resource "aws_alb" "app" {
  name            = "codemash-observability-app"
  subnets         = data.aws_subnet_ids.default.ids
  security_groups = [aws_security_group.http_ingress.id]
}

resource "aws_alb_listener" "app" {
  load_balancer_arn = aws_alb.app.arn
  port              = 80
  default_action {
    type             = "forward"
    target_group_arn = aws_alb_target_group.app.arn
  }
}

resource "aws_alb_target_group" "app" {
  name     = "spring-boot-app"
  vpc_id   = aws_default_vpc.default.id
  protocol = "HTTP"
  port     = 8080
  deregistration_delay = 5
  health_check {
    path    = "/actuator/health"
    port    = "8080"
    timeout = 10
  }
}

resource "aws_instance" "app" {
  ami                         = data.aws_ami.amazon-linux-2.id
  count                       = 1
  instance_type               = "t3a.large"
  associate_public_ip_address = true
  security_groups             = [aws_security_group.app.name]
  user_data                   = data.template_file.app_user_data.rendered
  iam_instance_profile        = aws_iam_instance_profile.default.name
  key_name                    = "fitz-personal-laptop"
  tags = {
    Name     = "spring-boot-app-${count.index}"
    Workload = "spring-boot"
  }
}



resource "aws_security_group" "app" {
  name = "app"
  ingress {
    from_port   = 8080
    protocol    = "tcp"
    to_port     = 8080
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

resource "aws_route53_record" "app" {
  name    = "app.${data.aws_route53_zone.default.name}"
  type    = "A"
  zone_id = data.aws_route53_zone.default.zone_id
  alias {
    name                   = aws_alb.app.dns_name
    zone_id                = aws_alb.app.zone_id
    evaluate_target_health = false
  }
}

resource "aws_ecs_service" "app" {
  name            = "app"
  cluster         = aws_ecs_cluster.default.id
  task_definition = aws_ecs_task_definition.app.arn
  desired_count   = 20
  placement_constraints {
    type       = "memberOf"
    expression = "attribute:workload==app"
  }
  load_balancer {
    container_name   = "app"
    container_port   = 8080
    target_group_arn = aws_alb_target_group.app.arn
  }
  health_check_grace_period_seconds = 30
  deployment_minimum_healthy_percent = 0
}


resource "aws_ecs_task_definition" "app" {
  family                = "app"
  network_mode          = "host"
  container_definitions = file("task-definitions/app.json")
}

