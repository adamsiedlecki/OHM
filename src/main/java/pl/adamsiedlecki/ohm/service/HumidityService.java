package pl.adamsiedlecki.ohm.service;

import lombok.RequiredArgsConstructor;
import org.influxdb.dto.Point;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.ohm.config.OhmConfigProperties;
import pl.adamsiedlecki.ohm.database.InfluxDatabaseService;
import pl.adamsiedlecki.ohm.database.model.HumidityPoint;
import pl.adamsiedlecki.ohm.dto.HumidityDto;
import pl.adamsiedlecki.ohm.utils.TimeUtil;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HumidityService {

    private final InfluxDatabaseService influxDatabaseService;
    private final OhmConfigProperties ohmConfigProperties;
    private final InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();

    public void saveHumidity(HumidityDto humidityDto) {
        Point point = Point.measurement(ohmConfigProperties.getInfluxDatabaseName())
                .addField("locationPlace", humidityDto.getLocationPlace())
                .addField("town", humidityDto.getTown())
                .addField("time", humidityDto.getTime().toInstant(TimeUtil.getOffset()).getEpochSecond())
                .addField("stationId", humidityDto.getStationId())
                .addField("stationName", humidityDto.getStationName())
                .addField("humidity", humidityDto.getHumidity())
                .build();
        influxDatabaseService.save(point);
    }

    public List<HumidityDto> getHumidityMeasurementsFromLastXHours(long hours) {
        long currentEpochSeconds = OffsetDateTime.now().toInstant().getEpochSecond();
        currentEpochSeconds -= hours * 60 * 60;
        QueryResult queryResult = influxDatabaseService.query(String.format("SELECT * FROM humidity WHERE time<%d ORDER BY time ASC", currentEpochSeconds));
        List<HumidityPoint> humidityPoints = resultMapper.toPOJO(queryResult, HumidityPoint.class);
        return humidityPoints.stream().map(this::convert).collect(Collectors.toList());
    }

    private HumidityDto convert(HumidityPoint point) {
        var time = LocalDateTime.from(point.getTime());
        return new HumidityDto(point.getLocationPlace(), point.getTown(), time, point.getStationId(), point.getStationName(), point.getHumidity());
    }
}
