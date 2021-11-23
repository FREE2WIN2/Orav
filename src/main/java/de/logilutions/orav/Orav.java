package de.logilutions.orav;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class Orav {
    private final long id;
    private final long playTimeDayOne;
    private final long playTime;
    private final LocalDateTime startDate;
}
