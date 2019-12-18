# JVM Application Monitoring with Micrometer, Prometheus, and Grafana


## Requirements

First of all, join the [Codemash Slack](https://codemash-slack.herokuapp.com/) if you haven't already, then join `#2020-jvm-observability-precompiler`.

You're going to want to install [Docker](https://www.docker.com/products/docker-desktop) and some version of java >= 8 (I recommend [sdkman](https://sdkman.io/sdks) for managing JVM versions).

Finally, from the root of the repository run `./gradlew precompile` and you should be all set.
Under the covers this runs some initial gradle builds for all the java projects to ensure that all maven dependencies have been downloaded,
and also does an initial pull of all required docker images.

There's a chance of some last minute updates, so you'll probably want to rerun that command shortly before CodeMash.

TODO: make the above section not a lie.