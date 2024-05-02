package yoon.docker.mapService.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PessimisticLockTimeOutException extends RuntimeException{

    private final String message;

    private final HttpStatus status;

    public PessimisticLockTimeOutException(String message, HttpStatus status){
        this.message = message;
        this.status = status;
    }


}
