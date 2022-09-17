package pl.adamsiedlecki.ohm.api.soap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.adamsiedlecki.ohm.soap.ImportHumidityRequest;
import pl.adamsiedlecki.ohm.soap.ImportHumidityResponse;
import pl.adamsiedlecki.ohm.soap.ResponseCode;
import pl.adamsiedlecki.ohm.soap.Result;

@Endpoint
@Slf4j
public class HumidityEndpoint {

    @PayloadRoot(namespace = "http://adamsiedlecki.pl/ohm/soap", localPart = "ImportHumidityRequest")
    @ResponsePayload
    public ImportHumidityResponse importHumidity(@RequestPayload ImportHumidityRequest request) {
        log.info(request.getTown() + " - " + request.getStationMessage());
        var result = new Result();
        result.setCode(ResponseCode.SUCCESS);

        var response = new ImportHumidityResponse();
        response.setResult(result);
        //return new JAXBElement(QName.valueOf(), )
        return response;
    }
}
