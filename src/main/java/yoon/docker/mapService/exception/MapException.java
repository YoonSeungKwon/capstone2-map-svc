package yoon.docker.mapService.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MapException extends RuntimeException{

    private final String message;

    private final HttpStatus status;

    public MapException(String message, HttpStatus status){
        this.message = message;
        this.status = status;
    }

}
