package yoon.docker.mapService.enums;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {

    //유효성 검사

    MAP_INDEX_NULL("지도의 인덱스가 비어있습니다.", HttpStatus.BAD_REQUEST),

    MEMBER_INDEX_NULL("초대하려는 멤버의 인덱스가 비어있습니다.", HttpStatus.BAD_REQUEST),

    MAP_TITLE_BLANK("지도의 제목이 비어있습니다.", HttpStatus.BAD_REQUEST),

    MAP_TITLE_NULL("지도의 제목이 비어있습니다.", HttpStatus.BAD_REQUEST),

    PRIVATE_MAP_DELETE("기본 지도는 삭제할 수 없습니다.", HttpStatus.BAD_REQUEST),

    PRIVATE_MAP_ADD("기본 지도에는 초대할 수 없습니다.", HttpStatus.BAD_REQUEST),


    //지도 예외

    MAP_NOT_FOUND("요청한 지도가 존재하지 않습니다", HttpStatus.NOT_FOUND),

    NOT_MAP_USER("해당 지도를 이용할 권한이 없습니다.", HttpStatus.FORBIDDEN),

    ALREADY_MAP_USER("이미 해당 지도에 가입되어 있습니다.", HttpStatus.BAD_REQUEST),

    //인증 예외

    MEMBER_NOT_FOUND("존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND),

    UNAUTHORIZED_ACCESS("인증되지 않은 접근입니다.", HttpStatus.UNAUTHORIZED),

    //서비스 호출 예외

    LOCK_TIMEOUT_ERROR("요청이 많습니다. 잠시 후 다시 시도해 주세요.", HttpStatus.LOCKED),

    INTERNAL_SERVER_ERROR("알 수 없는 에러입니다.", HttpStatus.INTERNAL_SERVER_ERROR),


    ;


    private final String message;

    private final HttpStatus status;

    ExceptionCode(String message, HttpStatus status){
        this.message = message;
        this.status = status;
    }


}

