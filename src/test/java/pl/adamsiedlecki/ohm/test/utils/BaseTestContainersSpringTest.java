package pl.adamsiedlecki.ohm.test.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.testcontainers.utility.DockerImageName;
import pl.adamsiedlecki.ohm.OhmApplication;
import pl.adamsiedlecki.ohm.config.OhmConfigProperties;
import pl.adamsiedlecki.ohm.database.InfluxDatabaseService;

@Configuration
@TestExecutionListeners(MockitoTestExecutionListener.class)
@Slf4j
@ContextConfiguration(classes = {OhmApplication.class, BaseTestContainersSpringTest.class})
public class BaseTestContainersSpringTest {

    private static final DockerImageName INFLUXDB_DOCKER_IMAGE = DockerImageName.parse("influxdb:2.4.0");

    private static final String DATABASE = "ohm";
    private static final String USER = "user";
    private static final String PASSWORD = "password";

//    @Bean
//    public InfluxDBContainer getInfluxContainer() {
//        var influxDBContainer = new InfluxDBContainer<>(INFLUXDB_DOCKER_IMAGE);
//        influxDBContainer.withDatabase(DATABASE)
//                .withUsername(USER)
//                .withPassword(PASSWORD)
//                .start();
//        return influxDBContainer;
//    }

    @Bean
    @Primary
    public InfluxDatabaseService getDataService(OhmConfigProperties ohmConfigProperties) {
//        log.info("Influx container url: " + influxDBContainer.getUrl());
        var properties = new OhmConfigProperties();
        properties.setInfluxDatabaseName(DATABASE);
        properties.setInfluxDatabaseUrl("http:10.0.0.20:18086"); // influxDBContainer.getUrl()
        properties.setInfluxDatabaseUser(USER);
        properties.setInfluxDatabasePassword(PASSWORD);
        properties.setInfluxDatabaseAdminToken("token");

        return new InfluxDatabaseService(properties);
    }

}
