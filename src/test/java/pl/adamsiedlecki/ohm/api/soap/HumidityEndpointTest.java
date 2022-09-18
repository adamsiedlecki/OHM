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
    void shouldImportHumidity() {
        // given
        var request = new ImportHumidityRequest();
        request.setTime(OffsetDateTime.now().toEpochSecond());
        request.setHumidity(60);
        request.setTown("Czachów");
        request.setStationId(20);
        request.setLocationPlaceId(1);

        //when
        var result = sut.importHumidity(request);

        //then
        Assertions.assertEquals(ResponseCode.SUCCESS, result.getResult().getCode());

        List<HumidityDto> humidityDtoList = humidityService.getHumidityMeasurementsFromLastXHours(1);
        Assertions.assertEquals(1, humidityDtoList.size());
        Assertions.assertEquals(BigDecimal.valueOf(60), humidityDtoList.get(0).getHumidity());

    }
}