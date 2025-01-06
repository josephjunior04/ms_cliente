package com.ms_cliente.ms_cliente.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ms_cliente.model.Client;
import com.ms_cliente.model.ClientRequest;
import com.ms_cliente.model.ClientResponse;
import com.ms_cliente.model.SubTypeClient;
import com.ms_cliente.model.TypeClient;
import com.ms_cliente.ms_cliente.exceptions.ClientAlreadyExistsExcepction;
import com.ms_cliente.ms_cliente.exceptions.ClientNotFoundException;
import com.ms_cliente.ms_cliente.repository.ClientRepository;
import com.ms_cliente.ms_cliente.service.ClientService;
import com.ms_cliente.ms_cliente.utils.ValidationRule;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

        private static final Logger LOGGER = LoggerFactory.getLogger(ClientService.class);
        /**
         * @repository repository
         */
        private final ClientRepository clientRepository;

        /**
         * @return response
         * @param clientRequest request to insert
         */
        public Mono<ClientResponse> insert(final ClientRequest clientRequest) {
                LOGGER.info("Saving client: {}", clientRequest);
                return validateClient(clientRequest)
                                .then(clientRepository
                                                .save(toEntityFromRequest(clientRequest))
                                                .map(this::toResponseFromEntity));
        }

        @Override
        public final Mono<ClientResponse> update(final String id, final ClientRequest clientRequest) {
                LOGGER.info("Update client: {}", clientRequest);
                return clientRepository.findById(id)
                                .flatMap(existingClient -> handleClientUpdate(existingClient, clientRequest))
                                .switchIfEmpty(handleErrorClientNotFound(id))
                                .map(this::toResponseFromEntity);
        }

        private Mono<Client> handleClientUpdate(final Client existingClient, final ClientRequest clientRequest) {
                return validateRules(clientRequest)
                        .then(Mono.just(existingClient))
                        .flatMap(client -> this.updateClient(client, clientRequest));
        }

        private Mono<Void> validateClient(final ClientRequest clientRequest) {
                return validateClientExists(clientRequest.getNroDocument())
                                .then(validateRules(clientRequest));
        }

        private Mono<Void> validateClientExists(final String nroDocument) {
                return clientRepository.findByNroDocument(nroDocument)
                                .flatMap(this::handleExistingClient);
        }

        private Mono<Void> handleExistingClient(final Client clientFound) {
                if (clientFound != null) {
                        return Mono.error(new ClientAlreadyExistsExcepction(
                                        String.format("Client with nroDocumento: %s already exists",
                                                        clientFound.getNroDocument())));
                }
                return Mono.empty();
        }

        private Mono<Void> validateRules(final ClientRequest clientRequest) {
                return Flux.fromIterable(getValidationRules())
                                .filter(rule -> rule.getPredicate().test(clientRequest))
                                .next()
                                .flatMap(rule -> Mono.error(new ClientNotFoundException(rule.getErrorMessage())))
                                .switchIfEmpty(Mono.empty())
                                .then();
        }

        private List<ValidationRule> getValidationRules() {
                return Arrays.asList(
                                new ValidationRule(
                                                isInvalidPersonalVip(),
                                                "Must be a PERSONAL type to be able to be VIP"),
                                new ValidationRule(
                                                isInvalidBusinessPyme(),
                                                "Must be a BUSINESS type to be able to be PYME"));
        }

        private Predicate<ClientRequest> isInvalidPersonalVip() {
                return request -> isSubType(request, SubTypeClient.VIP) && !isType(request, TypeClient.PERSONAL);
        }

        private Predicate<ClientRequest> isInvalidBusinessPyme() {
                return request -> isSubType(request, SubTypeClient.PYME) && !isType(request, TypeClient.BUSINESS);
        }

        private boolean isSubType(final ClientRequest request, final SubTypeClient expectedSubType) {
                return request.getSubType() != null
                                && expectedSubType.getValue().equals(request.getSubType().getValue());
        }

        private boolean isType(final ClientRequest request, final TypeClient expectedType) {
                return request.getType() != null
                                && expectedType.getValue().equals(request.getType().getValue());
        }

        private Mono<Client> updateClient(
                        final Client clientFound, final ClientRequest clientRequest) {
                clientFound.setName(clientRequest.getName());
                clientFound.setType(clientRequest.getType());
                clientFound.setSubType(clientRequest.getSubType());
                clientFound.setTypeDocument(clientRequest.getTypeDocument());
                clientFound.setNroDocument(clientRequest.getNroDocument());
                clientFound.setStatus(clientRequest.getStatus());

                return clientRepository.save(clientFound);
        }

        @Override
        public final Mono<ClientResponse> findById(final String id) {
                LOGGER.info("Start search client by id: {}", id);
                return clientRepository.findById(id)
                                .doOnNext(client -> LOGGER.info("Client found with id: {}", id))
                                .switchIfEmpty(handleErrorClientNotFound(id))
                                .map(this::toResponseFromEntity);
        }

        @Override
        public final Mono<Void> deleteById(final String id) {
                LOGGER.info("Delete client with id: {}", id);
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

        /**
         * @return Flux clients response
         */
        @Override
        public Flux<ClientResponse> findAll() {
                LOGGER.info("Return all clients");
                return clientRepository.findAll()
                                .map(this::toResponseFromEntity);
        }
}
