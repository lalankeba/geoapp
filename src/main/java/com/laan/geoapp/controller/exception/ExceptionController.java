package com.laan.geoapp.controller.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

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
}
