package main.java.com.app.service;

import main.java.com.app.models.Client;
import main.java.com.app.repository.interfaces.ClientRepository;
import main.java.com.app.repository.impl.ClientRepositoryImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ClientService {
    private ClientRepository clientRepository;

    public ClientService() {
        this.clientRepository = new ClientRepositoryImpl();
    }

    // Pour les tests
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client createClient(String name, String email, String phone, String address, BigDecimal monthlyIncome) {
        if (clientRepository.emailExists(email)) {
            throw new IllegalArgumentException("Email déjà utilisé");
        }

        Client client = new Client(name, email, phone, address, monthlyIncome);
        Client savedClient = clientRepository.save(client);

        if (savedClient != null) {
            return savedClient;
        }

        throw new RuntimeException("Erreur création client");
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }
}