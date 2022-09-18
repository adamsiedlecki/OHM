package pl.adamsiedlecki.ohm.dto;

import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
public class HumidityDto {
    String locationPlace;
    String town;
    LocalDateTime time;
    long stationId;
    String stationName;
    BigDecimal humidity;
}
