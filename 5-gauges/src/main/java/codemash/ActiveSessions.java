package codemash;

import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Tracks the number of active sessions, as well as the number of pings that have occurred for
 */
@Component
public class ActiveSessions {

    private final ConcurrentHashMap<UUID, AtomicLong> sessions = new ConcurrentHashMap<>();


    /**
     * Start tracking the given session
     */
    public void register(UUID id) {
        sessions.put(id, new AtomicLong(0));
    }

    /**
     * Increments the ping count for the given session
     */
    public void incrementPingCount(UUID id) {
        sessions.get(id).incrementAndGet();
    }


    /**
     * Stop tracking the given session
     */
    public void deregister(UUID id) {
        sessions.remove(id);
    }

    /**
     * @return the number of pings for the session with the most pings, or 0 if there are no active sessions
     */
    public long maxPingCount() {
        return sessions.values().stream().mapToLong(AtomicLong::longValue).max().orElse(0);
    }

    /**
     * @return the number of pings for the session with the least pings, or 0 if there are no active sessions
     */
    public long minPingCount() {
        return sessions.values().stream().mapToLong(AtomicLong::longValue).min().orElse(0);
    }

    /**
     * @return the total number of pings across all active sessions
     */
    public long totalPingCount() {
        return sessions.values().stream().mapToLong(AtomicLong::longValue).sum();
    }

    /**
     * @return the total number of active sessions
     */
    public long sessionCount() {
        return sessions.values().size();
    }


}
