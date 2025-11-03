package com.softwarebayya.AJAY.api;

import com.softwarebayya.AJAY.exception.InvalidDataException;
import com.softwarebayya.AJAY.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    static class Error {
        private final String reason;
        private final String message;

        public Error(String reason, String message) {
            this.reason = reason;
            this.message = message;
        }

        public String getReason() {
            return reason;
        }

        public String getMessage() {
            return message;
        }
    }

    // 400 - Bad Request
    @ExceptionHandler(InvalidDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Error handleInvalidDataException(InvalidDataException ex) {
        log.warn(ex.getMessage());
        return new Error(HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage());
    }

    // 404 - Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Error handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.warn(ex.getMessage());
        return new Error(HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage());
    }

    // 500 - Internal Server Error (catch-all)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Error handleUnknownException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new Error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), "Something went wrong.");
    }
}
