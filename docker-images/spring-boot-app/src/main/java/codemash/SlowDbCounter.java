package codemash;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * A simple database-based counter with artificial additional latency via `pg_sleep`
 */
@Component
public class SlowDbCounter {


    private final JdbcTemplate jdbcTemplate;
    private final Long id;

    /**
     * Connect to an existing counter with the given row id
     */
    public SlowDbCounter(JdbcTemplate jdbcTemplate, Long id) {
        this.jdbcTemplate = jdbcTemplate;
        this.id = id;
        ensureCounterExists(id);
    }

    /**
     * Create a new counter with a new unique ID
     */
    @Autowired
    public SlowDbCounter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.id = initCounter();
    }


    /**
     * @param latencySeconds the amount of latency to add to the db call via `pg_sleep`
     * @return the next counter value
     */
    public Long increment(float latencySeconds) {
        return jdbcTemplate.queryForObject(
                "UPDATE counter " +
                        "SET value = value + 1 " +
                        "FROM pg_sleep(?) " +
                        "WHERE id = ? " +
                        "RETURNING value;", Long.class, 5.5, id);
    }

    /**
     * Create a new row in the `counter` table
     *
     * @return the id of the row
     */
    private long initCounter() {
        return jdbcTemplate.queryForObject("INSERT INTO counter DEFAULT VALUES RETURNING id", Long.class);
    }

    /**
     * Make sure that the given id exists in the `counter` table
     *
     * @param id
     */
    private void ensureCounterExists(long id) {
        jdbcTemplate.update("INSERT INTO counter (id, value) values (?, 0) on conflict do nothing", id);
    }
}
