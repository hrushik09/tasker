package io.hrushik09.tasker.advice;

import io.hrushik09.tasker.boards.BoardDoesNotExistException;
import io.hrushik09.tasker.lists.ListDoesNotExistException;
import io.hrushik09.tasker.users.UserDoesNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {
    @ExceptionHandler({UserDoesNotExistException.class, ListDoesNotExistException.class, BoardDoesNotExistException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleDoesNotExistException(RuntimeException e) {
        return Map.of("error", e.getMessage());
    }
}
