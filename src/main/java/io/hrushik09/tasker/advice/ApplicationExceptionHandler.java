package io.hrushik09.tasker.advice;

import io.hrushik09.tasker.cards.InvalidFieldForUpdateCardException;
import io.hrushik09.tasker.cards.NotAllowedFieldForUpdateCardException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {
    @ExceptionHandler(DoesNotExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleDoesNotExistException(RuntimeException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(InvalidFieldForUpdateCardException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInvalidFieldForUpdateCardException(RuntimeException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(NotAllowedFieldForUpdateCardException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNotAllowedFieldForUpdateCardException(RuntimeException e) {
        return Map.of("error", e.getMessage());
    }
}
