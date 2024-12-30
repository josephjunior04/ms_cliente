package com.ms_cliente.ms_cliente.exceptions;

public class ClientNotFoundException extends RuntimeException {

    /**
     * @param message
     */
    public ClientNotFoundException(final String message) {
        super(message);
    }
}
