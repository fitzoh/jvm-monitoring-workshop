# 8: Binders

This one is pretty simple.
There's not a whole lot of instrumentation work to do, because the whole point of binders is that the work has already been done.

1. Add this dashboard to your Grafana instance: https://grafana.com/grafana/dashboards/4701
2. Observe that it doesn't work, because it requires an `application` label
3. Instead of adding a common label through Micrometer, set the `application` label using prometheus relabeling
   * You can either copy the `job` label, or just set a static value
   * Relabeling documentation available [here](https://prometheus.io/docs/prometheus/latest/configuration/configuration/#relabel_config)
