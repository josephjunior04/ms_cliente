package com.ms_cliente.ms_cliente.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public final class HandleResponseUtil {

    private HandleResponseUtil() {
        throw new UnsupportedOperationException(
            "This is a utility class and cannot be instantiated");
    }

    /**
     * @param <T>
     * @param status
     * @param body
     * @return Mono of Response Entity with specific status and body
     */
    public static <T> Mono<ResponseEntity<T>> handleResponse(
        final HttpStatus status, final Mono<T> body) {
        return body.map(response ->
            ResponseEntity.status(status).body(response));
    }

    /**
     * @param <T>
     * @param status
     * @param body
     * @return Mono of Response Entity with specific status and Flux<body>
     */
    public static <T> Mono<ResponseEntity<Flux<T>>> handleResponse(
        final HttpStatus status, final Flux<T> body) {
        return Mono.just(ResponseEntity.status(status).body(body));
    }
}
