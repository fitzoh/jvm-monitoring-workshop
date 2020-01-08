# 10: Remote Infrastructure

The last set of exercises depend upon a remote Prometheus instance and a couple instances of a remote Spring Boot app.
All processes are running in docker containers on [ECS](https://aws.amazon.com/ecs/).
The Spring Boot processes are behind an [ALB](https://docs.aws.amazon.com/elasticloadbalancing/latest/application/introduction.html),
and each [EC2](https://aws.amazon.com/ec2/) host is running the Prometheus [Node Exporter](https://github.com/prometheus/node_exporter) on port 9100.


1. Add remote Prometheus as a datasource to your local Grafana instance (http://prometheus.codemash-observability-workshop.com:9090/)[http://prometheus.codemash-observability-workshop.com:9090/]
2. Import the node exporter dashboard from https://grafana.com/grafana/dashboards/11074
3. Update your local Prometheus to [federate](https://prometheus.io/docs/prometheus/latest/federation/) from the remote Prometheus instance (you can start by just pulling down the spring boot metrics `'{job="spring-boot"}'`).
4. Do some kind of recording rule to make http timing histograms faster
   * start with buckets aggregated across all instances?
   * recording rule with offset
   * % change between now and previous value
5. Build a dashboard with HTTP latency
   * Alert when an instance does bad things
   * Add an annotation to show when chaos events happen
6. Add a blackbox exporter configuration to hit the public endpoint for the application ?