package codemash;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.MeterBinder;

public class ActiveSessionsMeterBinder implements MeterBinder {

    private final ActiveSessions activeSessions;

    public ActiveSessionsMeterBinder(ActiveSessions activeSessions) {
        this.activeSessions = activeSessions;
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        activeSessions.maxPingCount();
        activeSessions.minPingCount();
        activeSessions.sessionCount();
        activeSessions.totalPingCount();

        registry.gauge("sessions.active", activeSessions, ActiveSessions::sessionCount);
        registry.gauge("pings.per.session", Tags.of("statistic", "max"), activeSessions, ActiveSessions::maxPingCount);
        registry.gauge("pings.per.session", Tags.of("statistic", "min"), activeSessions, ActiveSessions::minPingCount);
        registry.gauge("pings.per.session", Tags.of("statistic", "sum"), activeSessions, ActiveSessions::totalPingCount);
    }
}
