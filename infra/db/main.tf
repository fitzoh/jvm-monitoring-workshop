resource "aws_rds_cluster" "app" {
  cluster_identifier = "codemash-demo-db"
  engine             = "aurora-postgresql"
  engine_mode        = "serverless"
  master_username    = "codemash"
  master_password    = "codemash"
  scaling_configuration {
    auto_pause     = true
    min_capacity   = 2
    max_capacity   = 2
    timeout_action = "ForceApplyCapacityChange"
  }
  vpc_security_group_ids = [aws_security_group.db.id]
}

resource "aws_security_group" "db" {
  name = "codemash-demo-db"
  ingress {
    from_port   = 5432
    to_port     = 5432
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}


