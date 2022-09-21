package pl.adamsiedlecki.ohm.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class OhmConfigProperties {

    @Value("${ohm.gen3.gateway.api.address}")
    private String gen3ApiAddress;

    @Value("${image.storage.path}")
    private String imageStoragePath;

    @Value("${ohm.default.chart.width:1200}")
    private int defaultChartWidth;

    @Value("${ohm.default.chart.height:800}")
    private int defaultChartHeight;

    @Value("${odg.base.path:http://10.0.0.20:8086/api/v1}")
    private String odgBasePath;

    @Value("${orchout.base.path:http://10.0.0.20:8087/api/v1}")
    private String orchoutBasePath;

    @Value("${influx.database.url:http://default.example}")
    private String influxDatabaseUrl;

    @Value("${influx.database.name:ohm}")
    private String influxDatabaseName;

    @Value("${influx.database.admin.token:xxx}")
    private String influxDatabaseAdminToken;
}
