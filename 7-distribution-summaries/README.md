# 7: Distribution Summaries

This application has a client that uploads files of random sizes, and and endpoint that receives them.
Track the size the of the payload, as well as the exponent used to generate the file size.

1. Instrument `TrafficGenerator` to record the exponent used
   * Set a min and max value
   * publish the full histogram
2. Instrument main application to record file size
   * Publish SLA buckets for each power of 2