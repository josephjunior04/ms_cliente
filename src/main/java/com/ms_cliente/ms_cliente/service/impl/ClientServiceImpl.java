package com.ms_cliente.ms_cliente.service.impl;

import org.springframework.stereotype.Service;

import com.ms_cliente.model.Client;
import com.ms_cliente.model.ClientRequest;
import com.ms_cliente.model.ClientResponse;
import com.ms_cliente.ms_cliente.exceptions.ClientNotFoundException;
import com.ms_cliente.ms_cliente.repository.ClientRepository;
import com.ms_cliente.ms_cliente.service.ClientService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public Flux<ClientResponse> findAll() {
        return clientRepository.findAll()
                .map(this::toResponseFromEntity);
    }

    @Override
    public Mono<ClientResponse> insert(ClientRequest clienteRequest) {
        return clientRepository.save(toEntityFromRequest(clienteRequest)).map(this::toResponseFromEntity);
    }

    @Override
    public Mono<ClientResponse> update(String id, ClientRequest clientRequest) {
        return clientRepository.findById(id)
                .flatMap(existingClient -> {
                    existingClient.setName(clientRequest.getName());
                    existingClient.setType(clientRequest.getType());
                    existingClient.setTypeDocument(clientRequest.getTypeDocument());
                    existingClient.setNroDocument(clientRequest.getNroDocument());
                    existingClient.setStatus(clientRequest.getStatus());

                    return clientRepository.save(existingClient);
                })
                .switchIfEmpty(Mono.error(
                        new ClientNotFoundException("Client with ID " + id + " not found")))
                .map(this::toResponseFromEntity);
    }

    @Override
    public Mono<ClientResponse> findById(String id) {
        return clientRepository.findById(id)
                .switchIfEmpty(Mono.error(new ClientNotFoundException("Client with ID " + id + " not found")))
                .map(this::toResponseFromEntity);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return clientRepository.findById(id)
                .switchIfEmpty(Mono.error(new ClientNotFoundException("Client with ID " + id + " not found")))
                .flatMap(existingClient -> {
                    return clientRepository.deleteById(existingClient.getId());
                });
    }

    private ClientResponse toResponseFromEntity(Client clientSaved) {
        ClientResponse clienteResponse = new ClientResponse();
        clienteResponse.setId(clientSaved.getId());
        clienteResponse.setName(clientSaved.getName());
        clienteResponse.setTypeDocument(clientSaved.getTypeDocument());
        clienteResponse.setType(clientSaved.getType());
        clienteResponse.setNroDocument(clientSaved.getNroDocument());
        clienteResponse.setStatus(clientSaved.getStatus());
        return clienteResponse;
    }

    private Client toEntityFromRequest(ClientRequest clientRequest) {
        Client client = new Client();
        client.setStatus(clientRequest.getStatus());
        client.setName(clientRequest.getName());
        client.setType(clientRequest.getType());
        client.setTypeDocument(clientRequest.getTypeDocument());
        client.setNroDocument(clientRequest.getNroDocument());
        return client;
    }
}
