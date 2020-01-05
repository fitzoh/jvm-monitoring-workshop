# 8: Actuator

Finally, a spring boot app with actuator metrics enabled.
All the instrumentation is done for you already!

This application has one endpoint with 3 mappings (`/first/{id}`, `/second/{id}`, `/third/{id}`), and a traffic generator that sends requests to all 3 endpoints (and an endpoint that doesn't exist, yielding a `404`).
Requests will take varying amounts of time, and some will randomly fail with exceptions.

1. Check out the source code, and pay special attention to `src/main/resources/application.yml` where all of the actuator config is located.
2. Add this dashboard to your Grafana instance: https://grafana.com/grafana/dashboards/10280
   * Once again, you'll need to add an `application` label... try updating `application.yml` this time
3. TODO: More stuff