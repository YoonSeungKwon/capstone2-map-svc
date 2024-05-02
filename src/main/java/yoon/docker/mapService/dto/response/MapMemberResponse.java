package yoon.docker.mapService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MapMemberResponse {

    private long idx;

    private String member;

    private String title;

    private LocalDateTime createdAt;

}
