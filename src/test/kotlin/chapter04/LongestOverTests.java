package chapter04;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static chapter04.Legs.findLongestLegOver;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class LongestOverTests {

    private final List<Leg> legs = List.of(
            leg("one hour", Duration.ofHours(1)),
            leg("one day", Duration.ofDays(1)),
            leg("two hours", Duration.ofHours(2))
    );

    private final Duration oneDay = Duration.ofDays(1);

    @Test
    public void is_absent_when_no_legs() {
        assertEquals(
                Optional.empty(),
                findLongestLegOver(Collections.emptyList(), Duration.ZERO)
        );
    }

    @Test
    public void is_absent_when_no_legs_long_enough() {
        assertEquals(
                Optional.empty(),
                findLongestLegOver(legs, oneDay)
        );
    }

    @Test
    public void is_longest_leg_when_one_match() {
        assertEquals(
                "one day",
                findLongestLegOver(legs, oneDay.minusMillis(1))
                        .orElseThrow().getDescription()
        );
    }

    @Test
    public void is_longest_leg_when_more_than_one_match() {
        assertEquals(
                "one day",
                findLongestLegOver(legs, oneDay.minusMillis(59))
                        .orElseThrow().getDescription()
        );
    }


    private Leg leg(String description, Duration duration)  {
        final ZonedDateTime start = ZonedDateTime.ofInstant(
                Instant.ofEpochSecond(ThreadLocalRandom.current().nextInt()),
                ZoneId.of("UTC"));
        return new Leg(description, start, start.plus(duration));
    }
}
