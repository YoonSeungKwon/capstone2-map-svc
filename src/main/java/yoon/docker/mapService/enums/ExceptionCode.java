package yoon.docker.mapService.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {



    ;


    private final String message;

    private final HttpStatus status;

    ExceptionCode(String message, HttpStatus status){
        this.message = message;
        this.status = status;
    }


}

