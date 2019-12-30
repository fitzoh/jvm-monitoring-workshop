# 3: Registries

This application demonstrates how you can use Micrometer `Registry`s to manipulate `Meter`s.

In this example, we have a `LoggingMeterRegistry` and a `PrometheusMeterRegistry` which have both been added to a `CompositeMeterRegistry`.
This means that any `Meter` added to the `CompositeMeterRegistry` will also be added to the other registries.

You have a couple tasks to accomplish by applying `MeterFilters` to the `MeterRegistry`'s listed above.

1. Add a `registry:prometheus` tag to the `PrometheusMeterRegistry`, and a `format:log` tag to the `LoggingMeterRegistry`
2. Add a `registry:log` tag to the `LoggingMeterRegistry`
3. Add a `conference:codemash` tag to all registries (should only need to update a single registry for this)
4. Fix the name of the `pormetheos.scrapes` `Counter` without modifying `RegistryApplication`

The last one will be the trickiest, as it doesn't have a prebuilt helper.
You will most likely find the tests useful.

You can find full docs on MeterFilters [here](https://micrometer.io/docs/concepts#_meter_filters).
