# 2: Dimensions

This application adds a small twist on the first.
Instead of a single counter, there are three counters with the same name but different tags.

The Prometheus configuration has also been modified a bit.
This app is actually being scraped by 2 different jobs (meaning it's recorded twice with different labels).

Note that this application (and most subsequent applications) contains some tests to help demonstrate concepts.

## Your tasks
1. Check out the source code (and tests!), start the app
2. Look at the new items present at the scrape endpoint
   * help/description string
   * labels
3. Experiment with the time series selectors documented [here](https://prometheus.io/docs/prometheus/latest/querying/basics/#time-series-selectors) (descriptions below shamelessly stolen from that link)
   * `=`: Select labels that are exactly equal to the provided string.
   * `!=`: Select labels that are not equal to the provided string.
   * `=~`: Select labels that regex-match the provided string.
   * `!~`: Select labels that do not regex-match the provided string.
4. Create a dashboard with the following graphs:
   * Just the time series from the `dimensional-app` `job` where `increment_by` = `one`
   * All the time series where the `conference` = `codemash`
   * All the time series that ended up at the wrong conference
   * All the time series where `increment_by` starts with `t`.
5. Read through the docs on [aggregation operations](https://prometheus.io/docs/prometheus/latest/querying/operators/#aggregation-operators)
6. Add the following graphs to the dashboard:
   * Sum of all the time series together
   * Sum of the time series by `conference` (excluding `codemash`)
   * The count of the total number of time series (try changing visualization type)
   * The count of time series by `conference` (try changing visualization type)
         