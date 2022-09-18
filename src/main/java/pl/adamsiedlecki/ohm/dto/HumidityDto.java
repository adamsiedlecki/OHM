package pl.adamsiedlecki.ohm.dto;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class HumidityDto {
    String locationPlace;
    String town;
    LocalDateTime time;
    long stationId;
    String stationName;
    double humidity;
}
