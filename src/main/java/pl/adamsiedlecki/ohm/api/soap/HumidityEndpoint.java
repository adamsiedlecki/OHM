package pl.adamsiedlecki.ohm.api.soap;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.adamsiedlecki.ohm.config.devices.Gen3DevicesInfo;
import pl.adamsiedlecki.ohm.config.locations.LocationPlacesInfo;
import pl.adamsiedlecki.ohm.dto.HumidityDto;
import pl.adamsiedlecki.ohm.exception.DeviceDoesNotMatchLocationPlaceException;
import pl.adamsiedlecki.ohm.exception.DeviceNotFoundInConfigurationException;
import pl.adamsiedlecki.ohm.exception.LocationPlaceNotFoundInConfigurationException;
import pl.adamsiedlecki.ohm.service.HumidityService;
import pl.adamsiedlecki.ohm.soap.ImportHumidityRequest;
import pl.adamsiedlecki.ohm.soap.ImportHumidityResponse;
import pl.adamsiedlecki.ohm.soap.ResponseCode;
import pl.adamsiedlecki.ohm.soap.Result;
import pl.adamsiedlecki.ohm.utils.TimeUtil;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Endpoint
@Slf4j
@RequiredArgsConstructor
public class HumidityEndpoint {

    private final HumidityService humidityService;
    private final LocationPlacesInfo locationPlacesInfo;
    private final Gen3DevicesInfo gen3DevicesInfo;

    @PayloadRoot(namespace = "http://adamsiedlecki.pl/ohm/soap", localPart = "ImportHumidityRequest")
    @ResponsePayload
    public ImportHumidityResponse importHumidity(@RequestPayload ImportHumidityRequest request) {
        log.info("Humidity import request received. town: {}, stationId: {}, humidity: {} ", request.getTown(), request.getStationId(), request.getHumidity());
        var result = new Result();
        result.setCode(ResponseCode.SUCCESS);

        try {
            humidityService.saveHumidity(convert(request));
        } catch (RuntimeException e) {
            log.error("Error while saving humidity: {}", e.getMessage());
            result.setCode(ResponseCode.ERROR);
            result.setDescription(e.getMessage());
        }

        var response = new ImportHumidityResponse();
        response.setResult(result);
        return response;
    }

    private HumidityDto convert(ImportHumidityRequest request) {
        var locationPlace = locationPlacesInfo.getById(request.getLocationPlaceId());
        if (locationPlace.isEmpty()) {
            throw new LocationPlaceNotFoundInConfigurationException();
        }
        var device = gen3DevicesInfo.getById(request.getStationId());
        if (device.isEmpty()) {
            throw new DeviceNotFoundInConfigurationException();
        }
        if (device.get().getLocationPlaceId() != locationPlace.get().getId()) {
            throw new DeviceDoesNotMatchLocationPlaceException();
        }
        var locationPlaceName = request.getLocationPlaceId() + " " + locationPlace.get().getName();
        var time = LocalDateTime.ofEpochSecond(request.getTime(), 0, TimeUtil.getOffset());
        return new HumidityDto(locationPlaceName, request.getTown(), time, request.getStationId(), device.get().getName(), request.getHumidity());
    }
}
