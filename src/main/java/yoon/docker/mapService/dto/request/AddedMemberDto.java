package yoon.docker.mapService.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AddedMemberDto {

    @NotNull(message = "MEMBER_INDEX_NULL")
    @NotBlank(message = "MEMBER_INDEX_BLANK")
    private long idx;

}
