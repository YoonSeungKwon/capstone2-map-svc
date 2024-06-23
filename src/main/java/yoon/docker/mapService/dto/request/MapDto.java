package yoon.docker.mapService.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class MapDto {

    @NotNull(message = "MAP_TITLE_NULL")
    @NotBlank(message = "MAP_TITLE_BLANK")
    private String title;

    private String color;

    private double lat;

    private double lon;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime selectedDate;

}
