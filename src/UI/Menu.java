package UI;

import util.InputHelpers;
import util.Validators;
import util.CardNumberGenerator;
import DAO.ClientDAO;
import DAO.CardDAO;
import entity.Client;
import entity.*;
import entity.enums.Status;
import service.CardService;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

public class Menu {
    // Centralized DAO instances
    private static final ClientDAO clientDAO = new ClientDAO();
    private static final CardDAO cardDAO = new CardDAO();
    private static final CardService cardService = new CardService(cardDAO);

    public static void menu(){
        while (true){
            System.out.println("\n====== Welcome to the Bank Card Management System ======\n");
            System.out.println("1. Create a Client");
            System.out.println("2. Create a Card");
            System.out.println("3. Make an Operation");
            System.out.println("4. View a Card History");
            System.out.println("5. Fraud Analysis");
            System.out.println("6. Suspend/Block a Card");
            System.out.println("7. Reports & Analytics");
            System.out.println("8. Exit");

            int choice = InputHelpers.readInt("\nPlease select an option (1-7): ");

            switch (choice){
                case 1:
                    createClient();
                    break;
                case 2:
                    createCardForClient();
                    break;
                case 3:
                    System.out.println("Make an Operation selected");
                    break;
                case 4:
                    System.out.println("View a Card History selected");
                    break;
                case 5:
                    System.out.println("Fraud Analysis selected");
                    break;
                case 6:
                    System.out.println("Suspend/Block a Card selected");
                    break;
                case 7:
                    System.out.println("Reports & Analytics selected");
                    break;
                case 8:
                    System.out.println("Exiting the system. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please select between 1-7.");
                    break;
            }
        }
    }

    //create new client
    private static void createClient(){
        System.out.println("\n====== Create a New Client ======\n");
        String name;
        do {
            name = InputHelpers.readString("Enter client's name: ");
            if (!Validators.isValidName(name)) {
                System.out.println("Invalid name. Please try again.");
            }
        } while (!Validators.isValidName(name));

        // Get and validate email
        String email;
        do {
            email = InputHelpers.readString("Enter client's email: ");
            if (!Validators.isValidEmail(email)) {
                System.out.println("Invalid email format. Please try again.");
            }
        } while (!Validators.isValidEmail(email));

        // Get and validate phone
        String phone;
        do {
            phone = InputHelpers.readString("Enter client's phone number (10 digits): ");
            if (!Validators.isValidPhoneNumber(phone)) {
                System.out.println("Invalid phone number format. Must be exactly 10 digits.");
            }
        } while (!Validators.isValidPhoneNumber(phone));

        clientDAO.insert(name, email, phone);
    }

    // get all clients as table
    private static void displayAllClients() {
        System.out.println("\n============ Available Clients ============\n");

        List<Client> clients = clientDAO.findAll();

        if (clients.isEmpty()) {
            System.out.println("No clients found in the database.");
            return;
        }

        // Print table header
        System.out.printf("%-5s %-20s %-30s %-15s%n", "ID", "Name", "Email", "Phone");
        System.out.println("---------------------------------------------------------------------");

        // Print each client
        for (Client client : clients) {
            System.out.printf("%-5d %-20s %-30s %-15s%n",
                client.id(),
                client.name(),
                client.email(),
                client.phone()
            );
        }

        System.out.println("---------------------------------------------------------------------");
        System.out.println("Total clients: " + clients.size());
    }

    // create a new card for existing client
    private static void createCardForClient() {
        displayAllClients();

        int clientId = InputHelpers.readInt("\nEnter the Client ID to create a card for: ");

        // Verify client exists
        Optional<Client> existClient = clientDAO.findById(clientId);

        if (existClient.isEmpty()) {
            System.out.println("Client with ID " + clientId + " not found!");
            return;
        }

        Client client = existClient.get();
        System.out.println("------Creating card for: " + client.name() + " (" + client.email() + ")------");

        // get card type
        System.out.println("\nSelect Card Type:");
        System.out.println("1. Credit Card");
        System.out.println("2. Debit Card");
        System.out.println("3. Prepaid Card");

        int cardTypeChoice = InputHelpers.readInt("Enter choice (1-3): ");

        // Generate card number and expiration date
        String cardNumber = CardNumberGenerator.generateCardNumber();
        LocalDateTime expirationDate = LocalDateTime.now().plusYears(5);

        Card newCard = null;

        switch (cardTypeChoice) {
            case 1: // Credit Card
                double monthlyLimit = InputHelpers.readDouble("Enter monthly limit: ");
                double interestRate = InputHelpers.readDouble("Enter interest rate (e.g., 0.15 for 15%): ");
                newCard = new CreditCard(0, cardNumber, expirationDate, Status.Active, clientId, monthlyLimit, interestRate);
                break;

            case 2: // Debit Card
                double dailyLimit = InputHelpers.readDouble("Enter daily limit: ");
                newCard = new DebitCard(0, cardNumber, expirationDate, Status.Active, clientId, dailyLimit);
                break;

            case 3: // Prepaid Card
                double balance = InputHelpers.readDouble("Enter initial balance: ");
                newCard = new PrepaidCard(0, cardNumber, expirationDate, Status.Active, clientId, balance);
                break;

            default:
                System.out.println("Invalid card type selection!");
                return;
        }
        cardService.addNewCard(newCard);

        System.out.println("-Card Number: " + cardNumber);
        System.out.println("-Expiration Date: " + expirationDate.toLocalDate());
    }

    // make an operation
}
