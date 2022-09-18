package pl.adamsiedlecki.ohm.database;

import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.ohm.config.OhmConfigProperties;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class InfluxDatabaseService {

    private InfluxDB influxDB;

    private final OhmConfigProperties ohmConfigProperties;

    public InfluxDatabaseService(OhmConfigProperties ohmConfigProperties) {
        this.ohmConfigProperties = ohmConfigProperties;
        initConnection();
    }

    public QueryResult query(String query) {
        return influxDB.query(new Query(query, ohmConfigProperties.getInfluxDatabaseName()));
    }

    public void save(Point point) {
        if (isConnectable()) {
            influxDB.write(point);
            influxDB.flush();
        } else {
            initConnection();
            if (isConnectable()) {
                influxDB.write(point);
            }
        }
    }

    private void initConnection() {
        try {
            influxDB = InfluxDBFactory.connect(ohmConfigProperties.getInfluxDatabaseUrl(),
                    ohmConfigProperties.getInfluxDatabaseUser(),
                    ohmConfigProperties.getInfluxDatabasePassword());
            influxDB.setDatabase(ohmConfigProperties.getInfluxDatabaseName());
            influxDB.enableBatch(100, 200, TimeUnit.MILLISECONDS);
            influxDB.createRetentionPolicy("defaultPolicy", "ohm", "forever", 1, true);
            influxDB.setRetentionPolicy("defaultPolicy");
        } catch (RuntimeException e) {
            log.error("Error while creating influxDB connection: {}", e.getMessage());
        }
    }

    private boolean isConnectable() {
        var pong = influxDB.ping();
        if (pong.getVersion().equalsIgnoreCase("unknown")) {
            log.error("Cannot connect to InfluxDb");
            return false;
        }
        return true;
    }
}
