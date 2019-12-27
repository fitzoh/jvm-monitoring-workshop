# JVM Application Monitoring with Micrometer, Prometheus, and Grafana


## Requirements
First of all, join the [Codemash Slack](https://codemash-slack.herokuapp.com/) if you haven't already, then join `#2020-jvm-observability-precompiler`.

You're going to want to install [Docker](https://www.docker.com/products/docker-desktop) and some version of java >= 8 (I recommend [sdkman](https://sdkman.io/sdks) for managing JVM versions).

Finally, from the root of the repository run `./gradlew precompile` and you should be all set.
Under the covers this runs some initial gradle builds for all the java projects to ensure that all maven dependencies have been downloaded,
and also does an initial pull of all required docker images.

There's a chance of some last minute updates, so you'll probably want to pull and re-run that command shortly before CodeMash.


## Gradle
There are a lot of moving parts in this workshop, and we're using [Gradle](https://gradle.org/) to orchestrate them.
We're using [The Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html), which means that you don't need to install Gradle on your machine.
Instead calling invoking `./gradlew` or `./gradlew.bat` automatically downloads and uses the appropriate gradle version.

All Gradle tasks should be run from the root of your repository.

The primary tasks you'll be concerned with are the [Docker Compose](https://docs.docker.com/compose/) tasks and the [Spring Boot](https://spring.io/projects/spring-boot) tasks found in each numbered directory (side note: all example application are Spring Boot applications).

### Docker Compose tasks

* `./gradlew docker-compose:up`: Starts the containers for Prometheus and Grafana and friends
* `./gradlew docker-compose:down`: Stops all the workshop containers
* `./gradlew docker-compose:pull`: Pulls the images for each of the containers we're going to use.  You shouldn't need to run this manually, as it's called by the `./gradlew precompile` task
* `./gradlew docker-compose:restartPrometheus`: Restarts the Prometheus container, clearing existing metrics and reloading the configuration file
* `./gradlew docker-compose:reloadPrometheus`: Sends a `SIGHUP` interrupt to the Prometheus container, reloading configuration without deleting existing data

### Spring Boot tasks

* `./gradlew <directory>:bootRun`: Starts the Spring Boot app in `<directory>`.  The `bootRun` tasks have a hook added to copy the directory's Prometheus config file to the `docker-compose` directory and reload.

Each spring boot app runs at port 8000 + the numeric prefix, so exercise `1` runs on port `8001`, and exercise `5` runs on port `8005`.

Tip: You can use prefixes for gradle tasks, so if you're feeling lazy you can run `./gradlew 1:bootRun` instead of `./gradlew 1-simple-e2e:bootRun`
