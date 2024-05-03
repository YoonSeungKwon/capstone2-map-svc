package yoon.docker.mapService.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MapDto {

    @NotNull(message = "MAP_TITLE_NULL")
    @NotBlank(message = "MAP_TITLE_BLANK")
    private String title;

}
