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


## Your tasks

1. Look at the source code. This is a minimal example.
2. Go to [http://localhost:8000/scrape](http://localhost:8000/scrape).
    * Figure out how it links back to the source code
    * Note that the counter goes up every time you access the endpoint
    * Note that prometheus also accesses the endpoint
3. Inspect `./prometheus.yml`
4. Go to [http://localhost:9090/](http://localhost:9090/), the Prometheus UI.  Explore the different options in the `status` menu,
especially  [targets](http://localhost:9090/targets)
5. Try running some queries from the [graph](http://localhost:9090/graph) page.
6. Open the Grafana UI at [http://localhost:3000](http://localhost:3000) and log in using the credentials `codemash:codemash`
7. Click the `Add data source` link and add your local Prometheus instance (the host should be `host.docker.internal:8001`)
8. Go to the Grafana [Explore](http://localhost:3000/explore) page and run some more (or the same basic queries).
9. Click the `+` icon on the navigation bar and create a new dashboard
10. Enter a query, hit the `save` icon and marvel at your fancy new dashboard
