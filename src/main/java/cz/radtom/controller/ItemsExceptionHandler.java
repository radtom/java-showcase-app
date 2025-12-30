package cz.radtom.controller;

import module java.base;

import cz.radtom.exception.BadSearchRequestException;
import cz.radtom.exception.ItemNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Controller advice for handling exceptions - mainly for including specific error only for selected exception types
 */
@ControllerAdvice
public class ItemsExceptionHandler {

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<Object> handleItemNotFound(ItemNotFoundException ex) {
        Map<String, Object> body = Map.of(
                "error", "Not found",
                "message", ex.getMessage()
        );

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadSearchRequestException.class)
    public ResponseEntity<Object> handleBadSearchRequest(BadSearchRequestException ex) {
        Map<String, Object> body = Map.of(
                "error", "Bad request",
                "message", ex.getMessage()
        );

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<Object> handleOptimisticLockingFailure(ObjectOptimisticLockingFailureException ex) {
        Map<String, Object> body = Map.of(
                "error", "Conflict",
                "message", "The data was updated by another user. Please refresh and try again."
        );

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
}
