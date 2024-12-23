package com.ms_cliente.ms_cliente.service;

import com.ms_cliente.model.ClientRequest;
import com.ms_cliente.model.ClientResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientService {
    Flux<ClientResponse> findAll();
    Mono<ClientResponse> insert(ClientRequest clienteRequest);
    Mono<ClientResponse> update(String id, ClientRequest clienteRequest);
    Mono<ClientResponse> findById(String id);
    Mono<Void> deleteById(String id);
}
