package pl.adamsiedlecki.ohm.gateway.pojo.devices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "gen3-devices")
public class Gen3DevicesInfo {

    private List<Gen3DeviceDto> devices = new ArrayList<>();

    public List<Gen3DeviceDto> getDevices() {
        return devices;
    }

    public void setDevices(List<Gen3DeviceDto> devices) {
        this.devices = devices;
    }

}
