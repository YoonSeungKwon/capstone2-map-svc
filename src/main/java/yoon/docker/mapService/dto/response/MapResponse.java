package yoon.docker.mapService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class MapResponse {

    private long idx;

    private long ownerIdx;

    private String title;

    private String color;

    private double lat;

    private double lon;

    private List<Integer> pins;

    private LocalDateTime selectedDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
