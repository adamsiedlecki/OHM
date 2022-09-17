package pl.adamsiedlecki.ohm.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;
import pl.adamsiedlecki.devices.gen3.client.api.Gen3DevicesApi;
import pl.adamsiedlecki.devices.gen3.client.invoker.ApiClient;
import pl.adamsiedlecki.ohm.config.OhmConfigProperties;

@Configuration
@RequiredArgsConstructor
public class Gen3DevicesConfiguration {

    private final OhmConfigProperties otmConfigProperties;
    private final RestTemplate restTemplate;

    @Bean
    @Primary
    public Gen3DevicesApi gen3DevicesApi() {
        return new Gen3DevicesApi(getApiClient());
    }

    private ApiClient getApiClient() {
        return new ApiClient(restTemplate)
                .setBasePath(otmConfigProperties.getGen3ApiAddress());
    }
}
