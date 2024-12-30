package com.ms_cliente.ms_cliente.utils;

import java.util.function.Predicate;

import com.ms_cliente.model.ClientRequest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ValidationRule {
    /**
     * @Predicate to validate
     */
    private final Predicate<ClientRequest> predicate;

    /**
     * @String Message error
     */
    private final String errorMessage;
}
