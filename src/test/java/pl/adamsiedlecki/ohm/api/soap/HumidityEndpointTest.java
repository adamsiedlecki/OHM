package pl.adamsiedlecki.ohm.api.soap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.adamsiedlecki.ohm.dto.HumidityDto;
import pl.adamsiedlecki.ohm.service.HumidityService;
import pl.adamsiedlecki.ohm.soap.ImportHumidityRequest;
import pl.adamsiedlecki.ohm.soap.ResponseCode;
import pl.adamsiedlecki.ohm.test.utils.BaseTestContainersSpringTest;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@SpringBootTest
class HumidityEndpointTest  {

    @Autowired
    private BaseTestContainersSpringTest baseTestContainersSpringTest;

    @Autowired
    private HumidityService humidityService;

    @Autowired
    private HumidityEndpoint sut;

    @Test
    void shouldImportHumidity() throws InterruptedException {
        // given
        var humidity = 60f;
        var town = "Czachów";
        var request = new ImportHumidityRequest();
        request.setTime(OffsetDateTime.now().toEpochSecond());
        request.setHumidity(humidity);
        request.setTown(town);
        request.setStationId(20);
        request.setLocationPlaceId(1);

        //when
        var result = sut.importHumidity(request);

        //then
        Thread.sleep(300); // make sure that batch is saved
        Assertions.assertEquals(ResponseCode.SUCCESS, result.getResult().getCode());

        List<HumidityDto> humidityDtoList = humidityService.getHumidityMeasurementsFromLastXHours(1);
        Assertions.assertEquals(1, humidityDtoList.size());
        Assertions.assertEquals(humidity, humidityDtoList.get(0).getHumidity());
        Assertions.assertEquals(town, humidityDtoList.get(0).getTown());

    }
}