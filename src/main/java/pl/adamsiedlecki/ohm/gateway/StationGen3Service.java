package pl.adamsiedlecki.ohm.gateway;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import pl.adamsiedlecki.devices.gen3.client.api.Gen3DevicesApi;
import pl.adamsiedlecki.devices.gen3.model.GenericMessageInput;
import pl.adamsiedlecki.devices.gen3.model.GenericMessageOutput;
import pl.adamsiedlecki.ohm.gateway.exceptions.Gen3DevicesApiException;
import pl.adamsiedlecki.ohm.gateway.exceptions.ResponseFromWrongStationException;
import pl.adamsiedlecki.ohm.gateway.exceptions.StationProbablyInDangerException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
@Configuration
public class StationGen3Service {

    private final Gen3DevicesApi gen3DevicesApi;

    public BigDecimal sendTemperatureRequest(long targetDevice) {
        GenericMessageInput input = new GenericMessageInput();
        input.setTid(targetDevice);
        input.setCmm(GenericMessageInput.CmmEnum.TR);
        return genericSend(input).getTp();
    }

    public BigDecimal sendHumidityRequest(long targetDevice) {
        GenericMessageInput input = new GenericMessageInput();
        input.setTid(targetDevice);
        input.setCmm(GenericMessageInput.CmmEnum.HR);
        return genericSend(input).getHu();
    }

    public BigDecimal sendBatteryVoltageRequest(long targetDevice) {
        GenericMessageInput input = new GenericMessageInput();
        input.setTid(targetDevice);
        input.setCmm(GenericMessageInput.CmmEnum.VR);
        return genericSend(input).getV();
    }

    private GenericMessageOutput genericSend(GenericMessageInput input) {
        if (input.getTid() == null) {
            log.error("GEN3 target station is not specified!");
        }
        GenericMessageOutput output;
        log.info("Sending GEN3 request: \n" + input);
        try {
            output = gen3DevicesApi.sendGenericRequest(input);
            log.info("Received GEN3 response: \n" + output);
            if (output == null) {
                throw new StationProbablyInDangerException();
            }
        } catch (RestClientException e) {
            log.error("Exception was thrown during generic GEN3 request.");
            throw new Gen3DevicesApiException(e.getMessage());
        }
        if (!output.getA().equals(input.getTid())) {
            log.error("The gen3 response is not from the right station!");
            throw new ResponseFromWrongStationException(String.format("Request was sent to %d, but response came from %d",input.getTid(), output.getA()));
        }
        return output;
    }

}
