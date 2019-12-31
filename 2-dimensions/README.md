# 2: Dimensions

This application adds a small twist on the first.
Instead of a single counter, there are multiple counters with the same name but different tags.
They also increment by different amounts.

The `primary` counters are always incremented on scrape, the `secondary` counters only occasionally/randomly.

Note that this application (and most subsequent applications) contains some tests to help demonstrate concepts.

## Your tasks
1. Check out the source code (and tests!), start the app (`./gradlew 2-dimensions:bootRun` from repo root)
2. Look at the new items present at the scrape endpoint
   * help/description string
   * labels
3. Experiment with the time series selectors documented [here](https://prometheus.io/docs/prometheus/latest/querying/basics/#time-series-selectors) (descriptions below shamelessly stolen from that link).
You might also find the samples in `selectors.sql` useful.
   * `=`: Select labels that are exactly equal to the provided string.
   * `!=`: Select labels that are not equal to the provided string.
   * `=~`: Select labels that regex-match the provided string.
   * `!~`: Select labels that do not regex-match the provided string.
4. Create a dashboard with the following graphs:
   * All the time series where the `conference` = `codemash` (which happens to be all of them)
   * All the time series where `delta` = `one`
   * All the time series where `delta` != `one` (trying finding multiple ways to accomplish this one)
5. Read through the docs on [aggregation operations](https://prometheus.io/docs/prometheus/latest/querying/operators/#aggregation-operators)
6. Add the following graphs to the dashboard:
   * Sum all the time series together
   * Sum all the time series by `method`
   * The top 3 time series (they might change occasionally)
   * The count of the total number of time series (try changing visualization type)
   * The count of time series by `delta` (try changing visualization type)
         