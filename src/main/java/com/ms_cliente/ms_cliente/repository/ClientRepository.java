package com.ms_cliente.ms_cliente.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.ms_cliente.model.Client;

public interface ClientRepository extends ReactiveMongoRepository<Client, String>{

}
