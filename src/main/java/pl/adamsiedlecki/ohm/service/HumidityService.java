package pl.adamsiedlecki.ohm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.ohm.database.InfluxDatabaseService;
import pl.adamsiedlecki.ohm.database.model.HumidityPoint;
import pl.adamsiedlecki.ohm.dto.HumidityDto;
import pl.adamsiedlecki.ohm.utils.TimeUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HumidityService {

    private final InfluxDatabaseService influxDatabaseService;

    public void saveHumidity(HumidityDto humidityDto) {
        HumidityPoint point = HumidityPoint.builder()
                .locationPlace(humidityDto.getLocationPlace())
                .town(humidityDto.getTown())
                .time(humidityDto.getTime().toInstant(TimeUtil.getOffset()))
                .stationId(humidityDto.getStationId())
                .stationName(humidityDto.getStationName())
                .humidityValue(humidityDto.getHumidity())
                .build();
        influxDatabaseService.save(point);
    }

    public List<HumidityDto> getHumidityMeasurementsFromLastXHours(long hours) {
        return influxDatabaseService.getFromLastHours(hours);
    }


}
