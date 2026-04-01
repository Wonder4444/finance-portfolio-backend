package com.wonder4.financeportfoliobackend.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** Handle logic interruptions triggered manually within Controller/Service */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<Void> handleBusinessException(BusinessException e) {
        log.warn("Business Exception Caught [Code: {}]: {}", e.getCode(), e.getMessage());
        return ApiResult.fail(e.getCode(), e.getMessage());
    }

    /** Handle @Valid / Validation structure failures */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<Void> handleValidationException(MethodArgumentNotValidException e) {
        log.warn("Validation error structure failure: {}", e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        String defaultMessage =
                "Validation failed: "
                        + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
        return ApiResult.fail(400, defaultMessage);
    }

    /** Stop database traces on duplicate constraints (e.g., registering duplicate symbol) */
    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<Void> handleDuplicateKeyException(DuplicateKeyException e) {
        log.warn("Database duplicate constraint violated: {}", e.getMessage());
        return ApiResult.fail(
                409,
                "Conflict: Record strictly violates unique constraints (likely already exists).");
    }

    /** Traps improperly formatted json payloads sent to endpoints */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<Void> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e) {
        log.warn("Malformed JSON payload triggered: {}", e.getMessage());
        return ApiResult.fail(
                400, "Malformed payload configuration. Verify your JSON structure and types.");
    }

    /** E.g. /api/users/abc failing to cast string 'abc' into expecting Long 'userId' */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<Void> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e) {
        log.warn("Path variable cast failure for [{}]: {}", e.getName(), e.getValue());
        return ApiResult.fail(
                400,
                "URL Parameter mismatch: Unable to convert '"
                        + e.getValue()
                        + "' into expected variable.");
    }

    /** Essential missing URL query parameters `?query=` */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.OK)
    public ApiResult<Void> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e) {
        log.warn("Required URL Query constraint violated: {}", e.getParameterName());
        return ApiResult.fail(
                400, "Missing required URL query param constraint: " + e.getParameterName());
    }

    /** Ultimate catch-all for severe faults and leaks */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult<Void> handleException(Exception e) {
        log.error("Unhandled Severe Fault Escalation [500]: ", e);
        return ApiResult.fail(500, "Server Encountered Unhandled State: " + e.getMessage());
    }
}
