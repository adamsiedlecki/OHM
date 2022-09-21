package pl.adamsiedlecki.ohm.dto;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class HumidityDto {
    String locationPlace;
    String town;
    LocalDateTime time;
    String stationId;
    String stationName;
    double humidity;
}
