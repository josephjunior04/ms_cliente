package com.ms_cliente.ms_cliente.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ms_cliente.model.ErrorResponse;

import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    /**
     * @param ex
     * @return Response Entity of Error Response with HttpStatus
     */
    @ExceptionHandler(ClientNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleClientNotFoundException(final ClientNotFoundException ex) {
        LOGGER.warn(ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("CLIENT_NOT_FOUND");
        errorResponse.setMessage(ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * @param ex
     * @return Response Entity of Error Response with HttpStatus
     */
    @ExceptionHandler(ClientAlreadyExistsExcepction.class)
    public final ResponseEntity<ErrorResponse> handleClientAlreadyExists(final ClientAlreadyExistsExcepction ex) {
        LOGGER.warn(ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("CLIENT_ALREADY_EXITS");
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
     * @return Response Entity of Invalid input request
     */
    @ExceptionHandler(ServerWebInputException.class)
    public final ResponseEntity<ErrorResponse> handleClientInvalidRequestException(final ServerWebInputException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("INVALID_INPUT_REQUEST");
        errorResponse.setMessage("Current request is invalid, verify the fields");
        return new ResponseEntity<>(
            errorResponse, HttpStatus.BAD_REQUEST);
    }

        /**
     * @param ex
     * @return Response Entity of General Exception
     */
    // @ExceptionHandler(Exception.class)
    // public final ResponseEntity<ErrorResponse> handleClientInvalidRequestException(final Exception ex) {
    //     ErrorResponse errorResponse = new ErrorResponse();
    //     errorResponse.setError("Excepcion");
    //     errorResponse.setMessage("Current request is invalid");

    //     return new ResponseEntity<>(
    //         errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    // }
}
