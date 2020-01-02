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
4. Make a new dashboard!
    * Show all the `prometheus_scrapes_total` series as instant vectors
    * Show 15 second rates of all those series... Before graphing them, what would you expect them to look like?
    * Simulate a couple restarts by triggering the `/simulate-restart` functions, see how the rates respond
    * Use the `resets` function to create a graph showing when the simulated restarts occured
    * Graph the `increase` of the non-dynamic counters at a couple different rates (`15s`, `30s`)... Can you guess what those graphs will look like/what values they will have?
    * The time series showing the value of the `AtomicInteger` has a similar set of labels as the `prometheus_scrapes_total` series.
    Try adding it to another time series (you might need to check out the docs on [1-1 vector matching](https://prometheus.io/docs/prometheus/latest/querying/operators/#one-to-one-vector-matches))
