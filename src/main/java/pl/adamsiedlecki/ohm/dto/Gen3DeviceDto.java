package pl.adamsiedlecki.ohm.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Gen3DeviceDto {
    private long id;
    private String name;
    private String longitude;
    private String latitude;
    private long locationPlaceId;
    private boolean hasBattery;
    private boolean canBeInDanger;
    private boolean external;
    private boolean isPrivate;
}
