package com.ms_cliente.ms_cliente.service;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ms_cliente.model.Client;
import com.ms_cliente.model.ClientRequest;
import com.ms_cliente.ms_cliente.exceptions.ClientNotFoundException;
import com.ms_cliente.ms_cliente.repository.ClientRepository;
import com.ms_cliente.ms_cliente.service.impl.ClientServiceImpl;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class ClientServiceImplTest {

    @InjectMocks
    private ClientServiceImpl clientService;

    @Mock
    private ClientRepository clientRepository;

    private static final String ID_MOCK = "6769a5bd6dba474a082ba1bc";
    private static final String NRO_DOCUMENT_MOCK = "7526121";
    private static final String NRO_DOCUMENT_UPDATED_MOCK = "7526121";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInsertClientSuccess() {
        // Arrange
        ClientRequest request = new ClientRequest();
        request.setNroDocument(NRO_DOCUMENT_MOCK);
        Client client = new Client();
        client.setNroDocument(NRO_DOCUMENT_MOCK);

        when(clientRepository.findByNroDocument(NRO_DOCUMENT_MOCK)).thenReturn(Mono.empty());
        when(clientRepository.save(any(Client.class))).thenReturn(Mono.just(client));

        // Act & Assert
        StepVerifier.create(clientService.insert(request))
                .expectNextMatches(response -> NRO_DOCUMENT_MOCK.equals(response.getNroDocument()))
                .verifyComplete();

        verify(clientRepository, times(1)).findByNroDocument(NRO_DOCUMENT_MOCK);
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void testUpdateClientSuccess() {
        // Arrange
        ClientRequest request = new ClientRequest();
        request.setNroDocument(NRO_DOCUMENT_MOCK);
        Client existingClient = new Client();
        existingClient.setId(ID_MOCK);
        existingClient.setNroDocument(NRO_DOCUMENT_UPDATED_MOCK);
        Client updatedClient = new Client();
        updatedClient.setId(ID_MOCK);
        updatedClient.setNroDocument(NRO_DOCUMENT_MOCK);

        when(clientRepository.findById(ID_MOCK)).thenReturn(Mono.just(existingClient));
        when(clientRepository.save(any(Client.class))).thenReturn(Mono.just(updatedClient));

        // Act & Assert
        StepVerifier.create(clientService.update(ID_MOCK, request))
                .expectNextMatches(response -> ID_MOCK.equals(response.getId())
                        && NRO_DOCUMENT_MOCK.equals(response.getNroDocument()))
                .verifyComplete();

        verify(clientRepository, times(1)).findById(ID_MOCK);
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void testUpdateClientClientNotFound() {
        // Arrange
        ClientRequest request = new ClientRequest();

        when(clientRepository.findById(ID_MOCK)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(clientService.update(ID_MOCK, request))
                .expectErrorMatches(throwable -> throwable instanceof ClientNotFoundException
                        && throwable.getMessage().contains(
                            String.format("Client with ID %s has not been found", ID_MOCK)))
                .verify();

        verify(clientRepository, times(1)).findById(ID_MOCK);
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void testFindByIdSuccess() {
        // Arrange
        Client client = new Client();
        client.setId(ID_MOCK);

        when(clientRepository.findById(ID_MOCK)).thenReturn(Mono.just(client));

        // Act & Assert
        StepVerifier.create(clientService.findById(ID_MOCK))
                .expectNextMatches(response -> ID_MOCK.equals(response.getId()))
                .verifyComplete();

        verify(clientRepository, times(1)).findById(ID_MOCK);
    }

    @Test
    void testFindByIdClientNotFound() {
        // Arrange
        when(clientRepository.findById(ID_MOCK)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(clientService.findById(ID_MOCK))
                .expectErrorMatches(throwable -> throwable instanceof ClientNotFoundException
                        && throwable.getMessage().contains(
                            String.format("Client with ID %s has not been found", ID_MOCK)))
                .verify();

        verify(clientRepository, times(1)).findById(ID_MOCK);
    }

    @Test
    void testDeleteByIdSuccess() {
        // Arrange
        Client client = new Client();
        client.setId(ID_MOCK);

        when(clientRepository.findById(ID_MOCK)).thenReturn(Mono.just(client));
        when(clientRepository.deleteById(ID_MOCK)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(clientService.deleteById(ID_MOCK))
                .verifyComplete();

        verify(clientRepository, times(1)).findById(ID_MOCK);
        verify(clientRepository, times(1)).deleteById(ID_MOCK);
    }

    @Test
    void testDeleteByIdClientNotFound() {
        // Arrange
        when(clientRepository.findById(ID_MOCK)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(clientService.deleteById(ID_MOCK))
                .expectErrorMatches(throwable -> throwable instanceof ClientNotFoundException
                        && throwable.getMessage().contains(
                            String.format("Client with ID %s has not been found", ID_MOCK)))
                .verify();

        verify(clientRepository, times(1)).findById(ID_MOCK);
        verify(clientRepository, never()).deleteById(ID_MOCK);
    }
}