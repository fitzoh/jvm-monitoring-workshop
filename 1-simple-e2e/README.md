# 1: Simple End to End

All commands should be run from the root of the repository.

First, start up the Docker containers:

`./graddlew docker-compose:up`

To start the sample application:

`./gradlew 1:bootRun`

The spring boot endpoint can then be accessed at http://localhost:8001/

The `bootRun` command has an added hook that causes it to:
* copy `prometheus.yml` to the Prometheus config volume
* send a `SIGHUP` command to the Prometheus container causing it to reload configuration
That pattern is used throughout this repository

The Prometheus UI can be accessed at http://localhost:9090/

The Grafana UI can be accessed at http://localhost:3000/ with credentials `codemash:codemash`.
