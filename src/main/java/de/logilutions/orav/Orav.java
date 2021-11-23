package de.logilutions.orav;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RequiredArgsConstructor
@Getter
public class Orav {
    private final long id;
    private final long playTimeDayOne;
    private final long playTime;
    private final LocalDateTime startDate;
    private final LocalTime earlyLogin;
    private final LocalTime latestLogin;
}
