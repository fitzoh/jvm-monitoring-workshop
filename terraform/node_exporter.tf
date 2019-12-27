resource "aws_ecs_task_definition" "node_exporter" {
  family                = "node-exporter"
  network_mode          = "host"
  container_definitions = file("task-definitions/node_exporter.json")

  volume {
    name      = "host"
    host_path = "/"
  }
  volume {
    name      = "sys"
    host_path = "/sys"
  }
  volume {
    name      = "proc"
    host_path = "/proc"
  }
}

resource "aws_ecs_service" "node_exporter" {
  name                = "node_exporter"
  cluster             = aws_ecs_cluster.default.id
  task_definition     = aws_ecs_task_definition.node_exporter.id
  scheduling_strategy = "DAEMON"
}