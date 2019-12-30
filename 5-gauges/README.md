# 5: Gauges

This application has a single [Server-sent events](https://developer.mozilla.org/en-US/docs/Web/API/Server-sent_events) endpoint that pushes a simple ping payload to all connected clients every second.

We want to keep track of the number of connected clients.
You can create additional connections by opening multiple browser tabs to [http://localhost:8005/](http://localhost:8005).

1. Review the source code and tests, then update the code to track the number of connected clients with a gauge
   * Hint: Look at the existing log statements for a hint on when clients connect and disconnect
2. Add a counter for the total number of ping events sent.   
3. Refactor your code to use the `ActiveSessions` class.  Add gauges for min/max/sum of pings for active sessions
4. TODO add some graphs?
