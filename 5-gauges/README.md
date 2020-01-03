# 5: Gauges

This application has a single [Server-sent events](https://developer.mozilla.org/en-US/docs/Web/API/Server-sent_events) endpoint that pushes a simple ping payload to all connected clients at a decaying rate (after 10 ms, then 20ms, then 30ms, etc).

We want to keep track of the number of connected clients.
You can create additional connections by opening multiple browser tabs to [http://localhost:8005/](http://localhost:8005).

1. Review the source code and tests, then update the code to track the number of connected clients with a gauge
   * Hint: Look at the existing log statements for a hint on when clients connect and disconnect
2. Add a counter for the total number of ping events sent.   
3. Refactor your code to use the `ActiveSessions` class.  Add gauges for min/max/sum of pings for active sessions
4. Dashboard!
   * Add a Gauge or SingleStat panel showing the current number of active sessions.
   If you're feeling fancy, add some value mappings to the `Visualization` section to display custom text values instead of the number.
   * Graph the 15s increase of the min and max gauges
   * Graph the difference between the 15s increase of min and max gauges (you'll need to do some [1-1 vector matching](https://prometheus.io/docs/prometheus/latest/querying/operators/#one-to-one-vector-matches) again, as they have different labels).
   * Add a graph with 2 series, one showing the max pings, and one showing where prometheus thinks it will be 30 seconds from now (using the `predict_linear` function).
   See what happens after a reset.
   * Graph the average number of pings per session (you have both the sum of pings across all sessions and the total nubmer of sessions)
