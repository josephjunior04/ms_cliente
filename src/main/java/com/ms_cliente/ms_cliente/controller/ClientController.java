package com.ms_cliente.ms_cliente.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.ms_cliente.controller.V1Api;
import com.ms_cliente.model.BalanceResponse;
import com.ms_cliente.model.ClientRequest;
import com.ms_cliente.model.ClientResponse;
import com.ms_cliente.ms_cliente.service.ClientService;
import com.ms_cliente.ms_cliente.utils.HandleResponseUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ClientController implements V1Api {

    /**
     * @service cliente service
     */
    private final ClientService clientService;

    /**
     * @return Mono Response Entity with status and Flux Client response
     */
    @Override
    public Mono<ResponseEntity<Flux<ClientResponse>>> findAll(
            final ServerWebExchange exchange) {
        return HandleResponseUtil
                .handleResponse(HttpStatus.OK, clientService.findAll());
    }

    /**
     * @return Mono Response Entity with status and Client response
     * @param clientRequest Request for insert client
     */
    @Override
    public Mono<ResponseEntity<ClientResponse>> insert(
            @Valid final Mono<ClientRequest> clientRequest,
            final ServerWebExchange exchange) {
        return clientRequest
                .flatMap(request -> HandleResponseUtil.handleResponse(HttpStatus.CREATED,
                        clientService.insert(request)));
    }

    /**
     * @return Mono Response Entity with status and Client response
     * @param client   Request for insert client
     * @param idClient Current id of client to update
     */
    @Override
    public Mono<ResponseEntity<ClientResponse>> update(
            final String idClient,
            @Valid final Mono<ClientRequest> client,
            final ServerWebExchange exchange) {
        return client
                .flatMap(request -> HandleResponseUtil.handleResponse(HttpStatus.OK,
                        clientService.update(idClient, request)));
    }

    /**
     * @return Mono Response Entity with status
     * @param idClient Current id of client to delete
     */
    @Override
    public Mono<ResponseEntity<Void>> deleteClientById(
            final String idClient, final ServerWebExchange exchange) {
        return HandleResponseUtil
                .handleResponse(
                        HttpStatus.NO_CONTENT, clientService.deleteById(idClient));
    }

    /**
     * @return Mono Response Entity with status
     * @param idClient Current id of client to delete
     */
    @Override
    public Mono<ResponseEntity<ClientResponse>> getClientById(final String idClient, final ServerWebExchange exchange) {
        return HandleResponseUtil
                .handleResponse(HttpStatus.OK, clientService.findById(idClient));
    }

    /**
     * @return Flux BalanceResponse by client
     * @param idClient Current id of client
     */
    @Override
    public Mono<ResponseEntity<Flux<BalanceResponse>>> getAllProductsByIdClient(final String idClient,
            final ServerWebExchange exchange) {
        return Mono.empty();
    }
}
