package yoon.docker.mapService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MemberResponse {

    private long idx;

    private String email;

    private String name;

    private String profile;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
