package yoon.docker.mapService.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AddedMemberDto {

    @NotNull(message = "MAP_INDEX_NULL")
    private long mapIdx;

    @NotNull(message = "MEMBER_INDEX_NULL")
    private long memberIdx;

}
