package yoon.docker.mapService.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import yoon.docker.mapService.enums.ExceptionCode;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<String> UserNameNotFoundError(){
        ExceptionCode code = ExceptionCode.MEMBER_NOT_FOUND;
        return new ResponseEntity<>(code.getMessage(), code.getStatus());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<String> validationException(MethodArgumentNotValidException e){
        BindingResult bindingResult  = e.getBindingResult();
        String exception = bindingResult.getAllErrors().get(0).getDefaultMessage();

        ExceptionCode code = switch (Objects.requireNonNull(exception)) {
            case "MEMBER_INDEX_BLANK" -> ExceptionCode.MEMBER_INDEX_BLANK;

            case "MEMBER_INDEX_NULL" -> ExceptionCode.MEMBER_INDEX_NULL;

            case "MAP_TITLE_BLANK" -> ExceptionCode.MAP_TITLE_BLANK;

            case "MAP_TITLE_NULL" -> ExceptionCode.MAP_TITLE_NULL;

            default -> ExceptionCode.INTERNAL_SERVER_ERROR;
        };

        return new ResponseEntity<>(code.getMessage(), code.getStatus());
    }

    @ExceptionHandler({PessimisticLockTimeOutException.class})
    public ResponseEntity<String> pessimisticLockException(PessimisticLockTimeOutException e){
        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }

    @ExceptionHandler({MapException.class})
    public ResponseEntity<String> mapException(MapException e){
        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }

    @ExceptionHandler({UnAuthorizedException.class})
    public ResponseEntity<String> authException(UnAuthorizedException e){
        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }

}
