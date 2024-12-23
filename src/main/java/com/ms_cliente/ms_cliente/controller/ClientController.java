package com.ms_cliente.ms_cliente.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.ms_cliente.api.V1Api;
import com.ms_cliente.model.ClientRequest;
import com.ms_cliente.model.ClientResponse;
import com.ms_cliente.ms_cliente.service.ClientService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ClientController implements V1Api {

    private final ClientService clientService;

    @Override
    public Mono<ResponseEntity<Flux<ClientResponse>>> findAll(ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok(clientService.findAll()));
    }

    @Override
    public Mono<ResponseEntity<ClientResponse>> insert(@Valid Mono<ClientRequest> clientRequest,
            ServerWebExchange exchange) {
        return clientRequest
                .flatMap(request -> clientService.insert(request)
                        .map(clienteResponse -> {
                            return ResponseEntity
                                    .status(HttpStatus.CREATED)
                                    .body(clienteResponse);
                        }));
    }

    @Override
    public Mono<ResponseEntity<ClientResponse>> update(String idClient, @Valid Mono<ClientRequest> cliente,
            ServerWebExchange exchange) {
        return cliente
                .flatMap(request -> clientService.update(idClient, request)
                        .map(clientResponse -> {
                            return ResponseEntity
                                    .status(HttpStatus.CREATED)
                                    .body(clientResponse);
                        }));
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteClientById(String idClient, ServerWebExchange exchange) {
        return clientService.deleteById(idClient)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

    @Override
    public Mono<ResponseEntity<ClientResponse>> getClientById(String idCliente, ServerWebExchange exchange) {
        return clientService.findById(idCliente)
                .map(clientResponse -> {
                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(clientResponse);
                });
    }

}
