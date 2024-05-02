package yoon.docker.mapService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MapResponse {

    private long idx;

    private String title;

    private String category;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
