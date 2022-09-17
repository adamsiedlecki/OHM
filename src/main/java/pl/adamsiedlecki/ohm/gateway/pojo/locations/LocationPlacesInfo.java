package pl.adamsiedlecki.ohm.gateway.pojo.locations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Getter
@Setter
@ConfigurationProperties(prefix = "location-places")
public class LocationPlacesInfo {

    private List<LocationPlaceDto> places;

    public Optional<LocationPlaceDto> getById(long id) {
        return places.stream().filter(loc -> loc.getId() == id).findFirst();
    }

}
