package pl.adamsiedlecki.ohm.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationPlaceDto {
    private long id;
    private String name;
    private String town;

    public String getNameWithTown() {
        return name + ", " + town;
    }
}
