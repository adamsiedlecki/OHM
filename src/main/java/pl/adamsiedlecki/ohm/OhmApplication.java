package pl.adamsiedlecki.ohm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class OhmApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Warsaw"));

		SpringApplication.run(OhmApplication.class, args);
	}

}
