# 6: Timers

This application has a single `/work` endpoint that returns a `200` response after a random amount of latency.
The requests are split into `fast`, `medium`, `slow`, and `really-slow` latency buckets (which we'll use as tags).


1. Review the source code and tests, then update the code to time the individual latency methods as noted by the comments
   * Skip the `LongTaskTimer` for now
2. Update the Timer configuration to latency histogram buckets for the SLA values defined by the `slaBuckets` helper function
3. Create a new Dashboard!
4. Graph the average duration for each latency bucket (total seconds / number of requests)
5. Create a Heatmap showing the number of requests that fall in the `medium` bucket.
There are a couple tricks you need to do to get a heatmap to render correctly:
   * Update the `Legend` on the `Query` tab to display only `{{le}}`` (the histogram bucket values)
   * Update the `Format` to `Heatmap` on the `Query` tab
   * On the `Visualization` tab set the visualization type to `Heatmap`
   * Finally, set the `Format` to `Time series buckets` instead of  the default `Time Series`
   * While you're here, you can try playing with some of the visualization options
6. Create a histogram that includes all of the latency buckets. 
Hint: You'll have to sum the time series together to make sure you end up with a single set of `le` values
7. Update the timer configuration again to publish precomputed values for `p75`, `p90`, and `p99`
8. Create a graph showing the published percentiles for each bucket.
   * Try summing the percentiles together across latency buckets.
   * That was a trap, you shouldn't sum precomputed percentiles! You should calculate them instead by combining histograms
9. Create a graph showing `p75`, `p90`, and `p99` for all endpoints together by summing their histograms together and using the `histogram_quantile` function (documented [here](https://prometheus.io/docs/prometheus/latest/querying/functions/#histogram_quantile)).    
10. Add a LongTaskTimer to the `really-slow` bucket
11. Create a graph tracking the number of in flight `really-slow` tasks.
