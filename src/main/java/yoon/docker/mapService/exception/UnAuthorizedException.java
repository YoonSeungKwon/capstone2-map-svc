package yoon.docker.mapService.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UnAuthorizedException extends RuntimeException{

    private final String message;
    private final HttpStatus status;

    public UnAuthorizedException(String message, HttpStatus status){
        this.message = message;
        this.status = status;
    }

}
