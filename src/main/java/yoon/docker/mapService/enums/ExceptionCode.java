package yoon.docker.mapService.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {


    //인증 에러

    UNAUTHORIZED_ACCESS("인증되지 않은 접근입니다.", HttpStatus.UNAUTHORIZED),

    //서비스 호출 에러

    INTERNAL_SERVER_ERROR("알 수 없는 에러입니다.", HttpStatus.INTERNAL_SERVER_ERROR),


    ;


    private final String message;

    private final HttpStatus status;

    ExceptionCode(String message, HttpStatus status){
        this.message = message;
        this.status = status;
    }


}

