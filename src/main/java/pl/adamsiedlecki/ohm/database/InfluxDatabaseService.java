package pl.adamsiedlecki.ohm.database;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.ohm.config.OhmConfigProperties;
import pl.adamsiedlecki.ohm.database.model.HumidityPoint;
import pl.adamsiedlecki.ohm.dto.HumidityDto;
import pl.adamsiedlecki.ohm.utils.TimeUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InfluxDatabaseService {

    private final OhmConfigProperties ohmConfigProperties;

    public InfluxDatabaseService(OhmConfigProperties ohmConfigProperties) {
        this.ohmConfigProperties = ohmConfigProperties;
    }

    public List<HumidityDto> getFromLastHours(long hours) {
        String flux = String.format("from(bucket:\"%s\") |> range(start:-%dh) |> filter(fn: (r) => r[\"_measurement\"] == \"humidity\")", "ohm-bucket", hours);
        log.info("Constructed flux query: {}", flux);
        try (var influxClient = buildConnection()) {
            QueryApi queryApi = influxClient.getQueryApi();
            var pointList = queryApi.query(flux, HumidityPoint.class);
            return pointList.stream().map(this::convert).collect(Collectors.toList());
        }
    }

    public InfluxDBClient buildConnection() {
        return InfluxDBClientFactory.create(ohmConfigProperties.getInfluxDatabaseUrl(),
                ohmConfigProperties.getInfluxDatabaseAdminToken().toCharArray(),
                "ohm-org",
                "ohm-bucket");
    }

    public void save(HumidityPoint point) {
        try (var influxClient = buildConnection()){
            WriteApiBlocking writeApi = influxClient.getWriteApiBlocking();
            writeApi.writeMeasurements(WritePrecision.MS, List.of(point));
        }
    }

    private HumidityDto convert(HumidityPoint point) {
        var time = LocalDateTime.ofInstant(point.getTime(), TimeUtil.getOffset());
        return new HumidityDto(point.getLocationPlace(), point.getTown(), time, point.getStationId(), point.getStationName(), point.getHumidityValue());
    }
}
