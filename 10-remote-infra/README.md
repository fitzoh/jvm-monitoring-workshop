# 10: Remote Infrastructure

TODO describe this better

1. Add remote Prometheus as a datasource to your local Grafana instance (http://prometheus.codemash-observability-workshop.com:9090/)[http://prometheus.codemash-observability-workshop.com:9090/]
2. Import the node exporter dashboard from https://grafana.com/grafana/dashboards/11074
3. Update your prometheus file to use ec2 service discovery.  You'll need to use relabeling to scrape from the public IP address instead of the private IP address (see (https://prometheus.io/docs/prometheus/latest/configuration/configuration/#ec2_sd_config)[https://prometheus.io/docs/prometheus/latest/configuration/configuration/#ec2_sd_config]).
4. Do some kind of recording rule to make http timing histograms faster
   * start with buckets aggregated across all instances?
   * recording rule with offset
   * % change between now and previous value
5. Build a dashboard with HTTP latency
   * Alert when an instance does bad things
   * Add an annotation to show when chaos events happen
6. Add a blackbox exporter configuration to hit the public endpoint for the application ?