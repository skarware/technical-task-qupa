package dev.skaringa.qupa.configuration.web;


import dev.skaringa.qupa.api.Error;
import dev.skaringa.qupa.api.ErrorCode;
import dev.skaringa.qupa.exception.BaseException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logErrorWithStackTrace(ex);
        return ResponseEntity.status(status).body(toErrors(ex));
    }

    @ExceptionHandler({IllegalArgumentException.class})
    protected ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex) {
        logErrorWithStackTrace(ex);
        Set<Error> errors = toErrors(ex);
        return ResponseEntity.status(resolveStatus(errors)).body(errors);
    }

    @ExceptionHandler({HttpMessageConversionException.class})
    protected ResponseEntity<Object> handleHttpMessageConversionException(HttpMessageConversionException ex) {
        logErrorWithStackTrace(ex);
        Set<Error> errors = toErrors(ex);
        return ResponseEntity.status(resolveStatus(errors)).body(errors);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        logErrorWithStackTrace(ex);
        Set<Error> errors = toErrors(ex);
        return ResponseEntity.status(resolveStatus(errors)).body(errors);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logErrorWithStackTrace(ex);
        Set<Error> errors = toErrors(ex);
        return ResponseEntity.status(resolveStatus(errors)).body(errors);
    }

    @ExceptionHandler({BaseException.class})
    protected ResponseEntity<Object> handleBase(BaseException ex) {
        logErrorWithStackTrace(ex);
        Set<Error> errors = toErrors(ex);
        return ResponseEntity.status(resolveStatus(errors)).body(errors);
    }

    private void logErrorWithStackTrace(Exception ex) {
        log.error("An error occurred", ex);
    }

    private Set<Error> toErrors(Exception ex) {
        if (ex instanceof IllegalArgumentException) {
            return Set.of(toIllegalArgumentExceptionError(ex));
        } else if (ex instanceof MethodArgumentTypeMismatchException) {
            return Set.of(toInvalidArgumentExceptionError(ex));
        } else if (ex instanceof HttpMessageConversionException) {
            return Set.of(toHttpMessageConversionExceptionError(ex));
        } else if (ex instanceof MethodArgumentNotValidException) {
            return toInvalidArgumentExceptionErrors((MethodArgumentNotValidException) ex);
        } else if (ex instanceof BaseException) {
            return Set.of(toError((BaseException) ex));
        }

        return Set.of(toUnexpectedError(ex));
    }

    private Error toIllegalArgumentExceptionError(Exception ex) {
        return Error.system(ErrorCode.ILLEGAL_ARGUMENT, ex.getMessage());
    }

    private Error toInvalidArgumentExceptionError(Exception ex) {
        return Error.system(ErrorCode.INVALID_ARGUMENT, ex.getMessage());
    }

    private Error toHttpMessageConversionExceptionError(Exception ex) {
        return Error.system(ErrorCode.INVALID_JSON, ex.getMessage());
    }

    private Set<Error> toInvalidArgumentExceptionErrors(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                .map(this::toInvalidArgumentError)
                .collect(Collectors.toUnmodifiableSet());
    }

    private Error toInvalidArgumentError(FieldError e) {
        return Error.system(ErrorCode.INVALID_ARGUMENT, "'" + e.getField() + "' " + e.getDefaultMessage());
    }

    private Error toError(BaseException ex) {
        return new Error(ex.getType(), ex.getCode(), ex.getMessage());
    }

    private Error toUnexpectedError(Exception ex) {
        return Error.unexpected(ex.getMessage());
    }

    private HttpStatus resolveStatus(Set<Error> errors) {
        if (errors.stream().anyMatch(error -> error.getCode() == ErrorCode.ILLEGAL_ARGUMENT)) {
            return HttpStatus.BAD_REQUEST;
        } else if (errors.stream().anyMatch(error -> error.getCode() == ErrorCode.INVALID_ARGUMENT)) {
            return HttpStatus.BAD_REQUEST;
        } else if (errors.stream().anyMatch(error -> error.getCode() == ErrorCode.NOT_FOUND)) {
            return HttpStatus.NOT_FOUND;
        }

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
