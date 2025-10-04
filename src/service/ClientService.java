package service;

import DAO.ClientDAO;
import entity.Client;
import java.util.Optional;

public class ClientService {
    ClientDAO clientDAO = new ClientDAO();

    public ClientService(ClientDAO clientDAO) {
        this.clientDAO = clientDAO;
    }

    // add new client
    public void createClient(String name, String email, String phone){
        clientDAO.insert(name, email, phone);
    }

    // get client by id
    public Optional<Client> getClientById(int id) {
        return clientDAO.findById(id);
    }

    // get all clients
    public void getAllClients() {
        clientDAO.findAll();
    }
}
