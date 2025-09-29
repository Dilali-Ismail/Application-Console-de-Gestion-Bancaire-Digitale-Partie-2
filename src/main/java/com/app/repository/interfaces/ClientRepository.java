package main.java.com.app.repository.interfaces;

import main.java.com.app.models.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {
    Client save(Client client);
    Optional<Client> findById(Long id);
    List<Client> findAll();
    boolean delete(Long id);
    boolean emailExists(String email);



}
