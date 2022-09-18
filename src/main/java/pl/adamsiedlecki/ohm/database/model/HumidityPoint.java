package pl.adamsiedlecki.ohm.database.model;

import lombok.Getter;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Measurement(name = "humidity")
@Getter
public class HumidityPoint {

    @Column(name = "locationPlace")
    private String locationPlace;
    @Column(name = "town")
    private String town;
    @Column(name = "time")
    private Instant time;
    @Column(name = "stationId")
    private long stationId;
    @Column(name = "stationName")
    private String stationName;
    @Column(name = "humidity")
    private BigDecimal humidity;
}
