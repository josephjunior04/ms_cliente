package com.ms_cliente.ms_cliente.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ms_cliente.model.ErrorResponse;

import org.springframework.web.bind.support.WebExchangeBindException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @param ex
     * @return Response Entity of Error Response with HttpStatus
     */
    @ExceptionHandler(ClientNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleClientNotFoundException(
            final ClientNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("Client not found");
        errorResponse.setMessage(ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * @param ex
     * @return Response Entity of Errors of Invalid Request
     */
    @ExceptionHandler(WebExchangeBindException.class)
    public final ResponseEntity<Object> handleClientInvalidRequestException(
            final WebExchangeBindException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().stream()
                .forEach((error) -> {
                    String fieldName = error.getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });

        return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * @param ex
     * @return Response Entity of General Exception
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse>
        handleClientInvalidRequestException(
        final Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("Excepcion");
        errorResponse.setMessage(ex.getMessage());

        return new ResponseEntity<>(
            errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
