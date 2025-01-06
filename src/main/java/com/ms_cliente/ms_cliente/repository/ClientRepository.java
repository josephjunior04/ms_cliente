package com.ms_cliente.ms_cliente.repository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.ms_cliente.model.Client;

import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends
    ReactiveMongoRepository<Client, String> {
        Mono<Client> findByNroDocument(String nroDocument);
}
