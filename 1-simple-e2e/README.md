# Demo 1: Simple End to End

All commands should be run from the root of the repository.

To start the sample application:

`./gradlew 1:bootRun`

The endpoint can then be accessed at http://localhost:8001/

To prepare the Prometheus config file:

`./gradlew 1:activatePrometheusConfig`

To start prometheus:

`./gradlew startPrometheus`

The prometheus UI can be accessed at http://localhost:9090/


To start Grafana:

`./gradlew startGrafana`

The Grafana UI can be accessed at http://localhost:3000/ with credentials `codemash:codemash`.
