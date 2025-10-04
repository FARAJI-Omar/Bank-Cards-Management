import DAO.ClientDAO;
import DAO.ConnectionDAO;
import UI.Menu;
import entity.Client;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        Menu.menu();
        // Test get client by id
        // Optional<Client> foundClient = ClientDAO.findById(5);
        // foundClient.ifPresentOrElse(client -> System.out.println("client name: " + client.name() +"\nclient email: " + client.email() + "\nclient phone: " + client.phone()),
                // () -> System.out.println("Client not found"));
    }
}