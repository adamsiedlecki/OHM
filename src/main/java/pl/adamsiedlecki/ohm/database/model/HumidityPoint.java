package pl.adamsiedlecki.ohm.database.model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Measurement(name = "humidity")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HumidityPoint {

    @Column(name = "locationPlace", tag = true)
    private String locationPlace;
    @Column(name = "town", tag = true)
    private String town;
    @Column(name = "time", timestamp = true)
    private Instant time;
    @Column(name = "stationId", tag = true)
    private String stationId;
    @Column(name = "stationName", tag = true)
    private String stationName;
    @Column(name = "value")
    private double humidityValue;
}
