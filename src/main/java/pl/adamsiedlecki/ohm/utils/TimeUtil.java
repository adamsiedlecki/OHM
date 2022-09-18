package pl.adamsiedlecki.ohm.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeUtil {

    public static ZoneOffset getOffset() {
        return OffsetDateTime.now().getOffset();
    }
}
