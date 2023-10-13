package com.laan.geoapp.controller.exception;

import com.laan.geoapp.exception.DuplicateElementException;
import com.laan.geoapp.exception.ElementNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Optional;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(ElementNotFoundException.class)
    public ProblemDetail onElementNotFoundException(ElementNotFoundException exception) {
        log.error("ElementNotFoundException occurred. {}", exception.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(DuplicateElementException.class)
    public ProblemDetail onDuplicateElementException(DuplicateElementException exception) {
        log.error("DuplicateElementException occurred. {}", exception.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ProblemDetail onMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Optional<FieldError> optionalFieldError = exception.getBindingResult().getFieldErrors().stream().findFirst();
        String message = exception.getMessage();
        if (optionalFieldError.isPresent()) {
            message = optionalFieldError.get().getDefaultMessage();
            message = (message == null) ? "" : message;
        }
        log.error("MethodArgumentNotValidException occurred. {}", message);
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ProblemDetail onHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        log.error("HttpRequestMethodNotSupportedException occurred. {}", exception.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail onMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        log.error("MethodArgumentTypeMismatchException occurred. {}", exception.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}
