package com.ms_cliente.ms_cliente.exceptions;

public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException(final String message) {
        super(message);
    }
}
