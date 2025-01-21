package com.ms_cliente.ms_cliente.service;

import com.ms_cliente.model.BalanceResponse;
import com.ms_cliente.model.ClientRequest;
import com.ms_cliente.model.ClientResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientService {
    /**
     * @return Flux of clients
     */
    Flux<ClientResponse> findAll();

    /**
     * @param clientRequest
     * @return Mono of Client saved
     */
    Mono<ClientResponse> insert(ClientRequest clientRequest);

    /**
     * @param id
     * @param clientRequest
     * @return Mono of Client updated
     */
    Mono<ClientResponse> update(String id, ClientRequest clientRequest);

    /**
     * @param id
     * @return Mono of Specific client response by ID
     */
    Mono<ClientResponse> findById(String id);

    /**
     * @param id
     * @return Mono<Void> when delete by Id
     */
    Mono<Void> deleteById(String id);

    /**
     * @param id Client
     * @return Mono<Void> when delete by Id
     */
    Flux<BalanceResponse> getAllProducts(String id);
}
