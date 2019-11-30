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
  vpc_id   = aws_default_vpc.default.id
  protocol = "HTTP"
  port     = 8080
}


resource "aws_launch_template" "app" {
  name          = "codemash-ecs-app"
  image_id      = data.aws_ami.amazon-linux-2.id
  instance_type = "t3a.small"
  key_name      = "fitz-personal-laptop"
  user_data     = base64encode(data.template_file.app_user_data.rendered)
  iam_instance_profile {
    name = aws_iam_instance_profile.default.name
  }
}

resource "aws_autoscaling_group" "default" {
  name               = "codemash-app"
  max_size           = 1
  min_size           = 1
  availability_zones = ["us-east-1a"]
  launch_template {
    id      = aws_launch_template.app.id
    version = "$Latest"
  }
  target_group_arns = []
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
