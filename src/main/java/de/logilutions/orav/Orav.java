package de.logilutions.orav;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@AllArgsConstructor
@Getter
public class Orav {
    private final long id;
    private final long playTimeDayOne;
    private final long playTime;
    private final LocalDateTime startDate;
    private final LocalTime earlyLogin;
    private final LocalTime latestLogin;
    @Setter
    private State state;
    private final long protectionTime;
    private final long protectionTimeAfterDayOne;

    public enum State{
        DEVELOPING,
        PREPARATION,
        COUNTDOWN,
        PROTECTION,
        RUNNING,
        OVER
    }
}
