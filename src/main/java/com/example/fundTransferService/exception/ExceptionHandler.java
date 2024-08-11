package com.example.fundTransferService.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.example.fundTransferService.business.dto.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler({CurrencyNotSupportedException.class})
    public ResponseEntity<ErrorResponse> handleCurrencyNotSupportedException(final CurrencyNotSupportedException exception) {
        return getErrorResponse(UNPROCESSABLE_ENTITY, exception.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({ExchangeStrategyException.class})
    public ResponseEntity<ErrorResponse> handleFeignException(final ExchangeStrategyException exception) {
        log.error("ExchangeStrategyException was thrown");
        return getErrorResponse(INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({InsufficientFundsException.class})
    public ResponseEntity<ErrorResponse> handleInsufficientFundsException(final InsufficientFundsException exception) {
        return getErrorResponse(UNPROCESSABLE_ENTITY, exception.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({InvalidAccountException.class})
    public ResponseEntity<ErrorResponse> handleInvalidAccountException(final InvalidAccountException exception) {
        return getErrorResponse(UNPROCESSABLE_ENTITY, exception.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({SameAccountException.class})
    public ResponseEntity<ErrorResponse> handleSameAccountException(final SameAccountException exception) {
        return getErrorResponse(UNPROCESSABLE_ENTITY, exception.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({UnableToRetrieveExchangeRateException.class})
    public ResponseEntity<ErrorResponse> handleUnableToRetrieveExchangeRateException(final UnableToRetrieveExchangeRateException exception) {
        log.error("UnableToRetrieveExchangeRateException was thrown, error: " + exception.getMessage());
        return getErrorResponse(SERVICE_UNAVAILABLE, exception.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        log.error("MethodArgumentNotValidException was thrown, error: " + exception.getMessage());
        String errorMessage = exception.getBindingResult().getFieldError().getField() + " " + exception.getBindingResult().getFieldError().getDefaultMessage();
        return getErrorResponse(BAD_REQUEST, errorMessage);
    }

    private static ResponseEntity<ErrorResponse> getErrorResponse(HttpStatus httpStatus, String exception) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(exception);
        errorResponse.setErrorCode(httpStatus.toString());
        errorResponse.setErrorDescription(httpStatus.getReasonPhrase());
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

}
