package pl.adamsiedlecki.ohm.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.ohm.config.devices.Gen3DevicesInfo;
import pl.adamsiedlecki.ohm.config.locations.LocationPlacesInfo;
import pl.adamsiedlecki.ohm.dto.Gen3DeviceDto;
import pl.adamsiedlecki.ohm.dto.HumidityDto;
import pl.adamsiedlecki.ohm.gateway.StationGen3Service;
import pl.adamsiedlecki.ohm.service.HumidityService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class HumiditySchedule {

    private final StationGen3Service stationGen3Service;
    private final Gen3DevicesInfo gen3DevicesInfo;
    private final LocationPlacesInfo locationPlacesInfo;
    private final HumidityService humidityService;

    /**
     * Checks humidity at every station every hour
     */
    @Scheduled(cron = "0 0 * * * *")
    public void checkHumidityHourly() {
        var localDevices= gen3DevicesInfo.getDevices().stream().filter(device -> !device.isExternal()).toList();
        for (Gen3DeviceDto device: localDevices) {

            try {
                BigDecimal humidity = stationGen3Service.sendHumidityRequest(device.getId());
                var humidityDto = prepare(humidity, device);
                humidityService.saveHumidity(humidityDto);
            } catch (RuntimeException ex) {
                log.error("Error while sending humidity request: {}", ex.getLocalizedMessage());
            }
        }
    }

    private HumidityDto prepare(BigDecimal humidity, Gen3DeviceDto device) {
        var locationPlace = locationPlacesInfo.getById(device.getLocationPlaceId());
        if(locationPlace.isEmpty()) {
            log.error("There is no such location place: {}", device.getLocationPlaceId());
            throw new IllegalStateException("Location place does not exist");
        }
        return new HumidityDto(locationPlace.get().getName(), locationPlace.get().getTown(), LocalDateTime.now(), ""+device.getId(), device.getName(), humidity.doubleValue());
    }

}
