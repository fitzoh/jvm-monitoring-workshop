# 4: Counters

This application demonstrates counters.
You've already seen a ton of counters.
But we're gonna go over them anyways!

This application contains a scrape endpoint with multiple counters that increment by different amounts on each scrape.

Counters don't normally reset, but we've included a `/simulate-restart` endpoint that sets the scrape counters to 0 via some registry magic to simulate an application restart.

1. Update the application to use appropriate tags for the existing counters
2. Add a counter to keep track of the current delta value.
   * Check the tests for a hint on how to accomplish this
   * Think about what name/tags that would make sense for this
   * Hint: You'll only need to declare this once
3. Take a look at the docs for [time series selectors](https://prometheus.io/docs/prometheus/latest/querying/basics/#time-series-selectors).
   * You've done a ton of queries using instant vectors, now we're formally introducing them as well as range vectors
   * You can't graph a range vector, so you'll need to wrap it in a function like `rate` or `increase` to graph it
   * Our scrape interval is `1s`, and range vectors should generally be 4x the scrape interval, so you should make sure your range vectors have a minimum of `4s`
   * For very small range intervals Grafana might skip some data points unless you lower the `Min time interval` on the Query panel.
   Generally speaking you're better off sticking with larger time ranges 
4. TODO
