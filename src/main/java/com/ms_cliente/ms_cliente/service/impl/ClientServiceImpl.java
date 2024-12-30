package com.ms_cliente.ms_cliente.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;

import com.ms_cliente.model.Client;
import com.ms_cliente.model.ClientRequest;
import com.ms_cliente.model.ClientResponse;
import com.ms_cliente.model.SubTypeClient;
import com.ms_cliente.model.TypeClient;
import com.ms_cliente.ms_cliente.exceptions.ClientNotFoundException;
import com.ms_cliente.ms_cliente.repository.ClientRepository;
import com.ms_cliente.ms_cliente.service.ClientService;
import com.ms_cliente.ms_cliente.utils.ValidationRule;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    /**
     * @repository repository
     */
    @NonNull
    private final ClientRepository clientRepository;

    /**
     * @return response
     * @param clientRequest request to insert
     */
    public Mono<ClientResponse> insert(
        final ClientRequest clientRequest) {
        return validateClient(clientRequest)
                .then(clientRepository.save(toEntityFromRequest(clientRequest))
                        .map(this::toResponseFromEntity));
    }

    @Override
    public final Mono<ClientResponse> update(
            final String id, final ClientRequest clientRequest) {
        return clientRepository.findById(id)
                .flatMap(existingClient -> updateClientIfValid(
                        existingClient, clientRequest))
                .switchIfEmpty(handleErrorClientNotFound(id))
                .map(this::toResponseFromEntity);
    }

    private Mono<Client> updateClientIfValid(
            final Client existingClient, final ClientRequest clientRequest) {
        return validateClient(clientRequest)
                .then(Mono.just(existingClient))
                .flatMap(client -> this.updateClient(client, clientRequest));
    }

    private Mono<Void> validateClient(final ClientRequest clientRequest) {
        return Flux.fromIterable(getValidationRules())
                .filter(rule -> rule.getPredicate().test(clientRequest))
                .next()
                .flatMap(rule -> Mono.error(
                    new ClientNotFoundException(rule.getErrorMessage())))
                .switchIfEmpty(Mono.empty())
                .then();
    }

    private Mono<Client> updateClient(
            final Client clientFound, final ClientRequest clientRequest) {
        clientFound.setName(clientRequest.getName());
        clientFound.setType(clientRequest.getType());
        clientFound.subType(clientRequest.getSubType());
        clientFound.setTypeDocument(clientRequest.getTypeDocument());
        clientFound.setNroDocument(clientRequest.getNroDocument());
        clientFound.setStatus(clientRequest.getStatus());

        return clientRepository.save(clientFound);
    }

    @Override
    public final Mono<ClientResponse> findById(final String id) {
        return clientRepository.findById(id)
                .switchIfEmpty(handleErrorClientNotFound(id))
                .map(this::toResponseFromEntity);
    }

    @Override
    public final Mono<Void> deleteById(final String id) {
        return clientRepository.findById(id)
                .switchIfEmpty(handleErrorClientNotFound(id))
                .flatMap(existingClient -> {
                    return clientRepository.deleteById(existingClient.getId());
                });
    }

    private ClientResponse toResponseFromEntity(
            final Client clientSaved) {
        ClientResponse clienteResponse = new ClientResponse();
        clienteResponse.setId(clientSaved.getId());
        clienteResponse.setName(clientSaved.getName());
        clienteResponse.setTypeDocument(clientSaved.getTypeDocument());
        clienteResponse.setType(clientSaved.getType());
        clienteResponse.setSubType(clientSaved.getSubType());
        clienteResponse.setNroDocument(clientSaved.getNroDocument());
        clienteResponse.setStatus(clientSaved.getStatus());
        return clienteResponse;
    }

    private Client toEntityFromRequest(final ClientRequest clientRequest) {
        Client client = new Client();
        client.setStatus(clientRequest.getStatus());
        client.setName(clientRequest.getName());
        client.setType(clientRequest.getType());
        client.setSubType(clientRequest.getSubType());
        client.setTypeDocument(clientRequest.getTypeDocument());
        client.setNroDocument(clientRequest.getNroDocument());
        return client;
    }

    private Mono<Client> handleErrorClientNotFound(final String id) {
        String errorMessage = String.format(
            "Client with ID %s has not been found", id);
        return Mono.error(new ClientNotFoundException(errorMessage));
    }

    private List<ValidationRule> getValidationRules() {
        Predicate<ClientRequest> predicateIsValidPersonalVip =
            (request) -> request.getSubType() != null
        && request.getSubType().getValue().equals(SubTypeClient.VIP.getValue())
        && !request.getType().getValue().equals(TypeClient.PERSONAL.getValue());
        String errorMessage = "Must be a PERSONAL type to be able to be VIP";
        Predicate<ClientRequest> predicateIsValidBusinessPyme =
         (request) -> request.getSubType() != null
        && request.getSubType().getValue().equals(SubTypeClient.PYME.getValue())
        && !request.getType().getValue().equals(TypeClient.BUSINESS.getValue());
        String message = "Must be a BUSINESS type to be able to be PYME";
        return Arrays.asList(
                new ValidationRule(
                        predicateIsValidPersonalVip,
                        errorMessage),
                new ValidationRule(
                        predicateIsValidBusinessPyme,
                        message));
    }

    /**
     * @return Flux clients response
     */
    @Override
    public Flux<ClientResponse> findAll() {
        return clientRepository.findAll()
                .map(this::toResponseFromEntity);
    }
}
