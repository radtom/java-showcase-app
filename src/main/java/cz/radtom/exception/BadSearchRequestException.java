package cz.radtom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadSearchRequestException extends RuntimeException {
    public BadSearchRequestException(String message) {
        super(message);
    }
}
