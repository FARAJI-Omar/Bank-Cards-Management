package UI;

import DAO.OperationDAO;
import service.ClientService;
import service.FraudService;
import service.OperationService;
import util.InputHelpers;
import util.Validators;
import util.CardNumberGenerator;
import util.DateFormatter;

import util.FraudRules;
import DAO.ClientDAO;
import DAO.CardDAO;
import DAO.FraudAlertDAO;
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
    private static final OperationDAO operationDAO = new OperationDAO();
    private static final FraudAlertDAO fraudAlertDAO = new FraudAlertDAO();
    private static final CardService cardService = new CardService(cardDAO);
    private static final FraudService fraudService = new FraudService(fraudAlertDAO);
    private static final ClientService clientService = new ClientService(clientDAO);
    private static final FraudRules fraudRules = new FraudRules(fraudAlertDAO, operationDAO);
    private static final OperationService operationService = new OperationService(operationDAO, cardDAO, fraudService, fraudRules);

    public static void menu(){
        while (true){
            System.out.println("\n====== Welcome to the Bank Card Management System ======\n");
            System.out.println("1. Create a Client");
            System.out.println("2. Create a Card");
            System.out.println("3. Make an Operation");
            System.out.println("4. View a Card History");
            System.out.println("5. Fraud Analysis");
            System.out.println("6. Activate/Suspend/Block a Card");
            System.out.println("7. Reports & Analytics");
            System.out.println("0. Exit");

            int choice = InputHelpers.readInt("\nPlease select an option (1-7, 0 to exit): ");

            switch (choice){
                case 1:
                    createClient();
                    break;
                case 2:
                    createCardForClient();
                    break;
                case 3:
                    operationMenu();
                    break;
                case 4:
                    cardHistory();
                    break;
                case 5:
                    displayFraudAlerts();
                    break;
                case 6:
                    updateCardStatus();
                    break;
                case 7:
                    System.out.println("Reports & Analytics selected");
                    break;
                case 0:
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
    private static void operationMenu(){
        while (true){
            System.out.println("\n=== Choose Operation ===\n");
            System.out.println("1. Purchase");
            System.out.println("2. Withdraw");
            System.out.println("3. Online Purchase");
            System.out.println("0. Exit");

            int choice = InputHelpers.readInt("\nPlease select an option (1-3, 0 to exit): ");

            switch (choice) {
                case 1: {
                    System.out.println("\n== Purchase ==\n");
                    displayAllClients();
                    int clientId = InputHelpers.readInt("\nEnter the Client ID to select a card: ");
                    Optional<Client> existClient = clientService.getClientById(clientId);
                    if (existClient.isEmpty()) {
                        System.out.println("\nClient with ID " + clientId + " not found! Please try again.");
                        continue;
                    }
                    int cardId = selectClientCard(clientId);
                    if (cardId == -1) continue;

                    double amount = InputHelpers.readDouble("Enter purchase amount: ");
                    String location = InputHelpers.readString("Enter purchase location: ");
                    operationService.purchase(cardId, amount, location);
                    return;
                }
                case 2: {
                    System.out.println("\n== Withdraw ==\n");
                    displayAllClients();

                    int clientId = InputHelpers.readInt("\nEnter the Client ID to select a card: ");
                    Optional<Client> existClient = clientService.getClientById(clientId);
                    if (existClient.isEmpty()) {
                        System.out.println("\nClient with ID " + clientId + " not found! Please try again.");
                        continue;
                    }

                    int cardId = selectClientCard(clientId);
                    if (cardId == -1) continue;

                    double amount = InputHelpers.readDouble("Enter withdrawal amount: ");
                    String location = InputHelpers.readString("Enter withdrawal location: ");

                    operationService.withdraw(cardId, amount, location);
                    return;
                }
                case 3: {
                    System.out.println("\n== Online Purchase ==\n");
                    displayAllClients();

                    int clientId = InputHelpers.readInt("\nEnter the Client ID to select a card: ");
                    Optional<Client> existClient = clientService.getClientById(clientId);
                    if (existClient.isEmpty()) {
                        System.out.println("\nClient with ID " + clientId + " not found! Please try again.");
                        continue;
                    }

                    int cardId = selectClientCard(clientId);
                    if (cardId == -1) continue;

                    double amount = InputHelpers.readDouble("Enter online purchase amount: ");
                    String location = InputHelpers.readString("Enter website/store name: ");

                    operationService.onlinePurchase(cardId, amount, location);
                    return;
                }
                case 0:
                    System.out.println("Exiting operation menu.");
                    return;
                default:
                    System.out.println("Invalid option. Please select between 1-3.");
                    break;
            }
        }
    }

    // Display all cards of a client
    private static int selectClientCard(int clientId) {
        List<Card> cards = cardDAO.getCardsByClientId(clientId);
        if (cards.isEmpty()) {
            System.out.println("No cards found for this client.");
            return -1;
        }

        System.out.println("\n============ Client's Cards ============\n");
        System.out.printf("%-5s %-15s %-15s %-12s %-20s %-10s%n",
                "No", "Card ID", "Card Type", "Status", "Limit/Balance", "Expires");

        System.out.println("----------------------------------------------------------------------------------");

        int index = 1;
        for (Card card : cards) {
            String type;
            String value;

            if (card instanceof CreditCard credit) {
                type = "Credit";
                value = String.format("Monthly: %.2f", credit.getMonthlyLimit());
            } else if (card instanceof DebitCard debit) {
                type = "Debit";
                value = String.format("Daily: %.2f", debit.getDailyLimit());
            } else if (card instanceof PrepaidCard prepaid) {
                type = "Prepaid";
                value = String.format("Balance: %.2f", prepaid.getBalance());
            } else {
                type = "Unknown";
                value = "-";
            }

            System.out.printf("%-5d %-15d %-15s %-12s %-20s %-10s%n",
                    index++,
                    card.getId(),
                    type,
                    card.getStatus(),
                    value,
                    card.getExpirationDate().toLocalDate());
        }
        System.out.println("----------------------------------------------------------------------------------");

        int selection;
        do {
            selection = InputHelpers.readInt("Enter card number from list (1-" + cards.size() + "): ");
            if (selection < 1 || selection > cards.size()) {
                System.out.println("Invalid choice. Try again.");
            }
        } while (selection < 1 || selection > cards.size());

        return cards.get(selection - 1).getId();
    }

    private static void cardHistory(){
        System.out.println("\n== Card History ==\n");
        while (true) {
            displayAllClients();
            int clientId = InputHelpers.readInt("\nEnter the Client ID to select a card (0 to go back): ");

            if (clientId == 0) {
                return;
            }

            Optional<Client> existClient = clientService.getClientById(clientId);
            if (existClient.isEmpty()) {
                System.out.println("\nClient with ID " + clientId + " not found! Please try again.");
                continue;
            }

            int cardId = selectClientCard(clientId);
            if (cardId == -1) {
                continue;
            }

            // Display card operations
            getCardOperations(cardId);

            break;
        }
    }

    private static void getCardOperations(int cardId){
        List<CardOperation> operations = operationDAO.getCardOperationsHistory(cardId);
        if (operations.isEmpty()) {
            System.out.println("\nNo operations found for this card.");
            return;
        }

        System.out.println("\n============ Card Operations History ============\n");
        System.out.printf("%-5s %-20s %-12s %-18s %-25s%n",
                "#", "Date", "Amount", "Type", "Location/Online");

        System.out.println("--------------------------------------------------------------------------------");

        int index = 1;
        for (CardOperation operation : operations) {
            System.out.printf("%-5d %-20s %-12.2f %-18s %-25s%n",
                    index++,
                    DateFormatter.formatDateTime(operation.operationDate()),
                    operation.amount(),
                    operation.type(),
                    operation.location());
        }
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Total operations: " + operations.size());
    }

    private static void updateCardStatus() {
        System.out.println("\n== Activate/Suspend/Block Card ==\n");
        while (true) {
            displayAllClients();
            int clientId = InputHelpers.readInt("\nEnter the Client ID to select a card (0 to go back): ");

            if (clientId == 0) {
                return;
            }

            Optional<Client> existClient = clientService.getClientById(clientId);
            if (existClient.isEmpty()) {
                System.out.println("\nClient with ID " + clientId + " not found! Please try again.");
                continue;
            }

            int cardId = selectClientCard(clientId);
            if (cardId == -1) {
                continue;
            }

            // update card status
            while (true) {
                System.out.println("\nSelect new status:");
                System.out.println("1. Activate");
                System.out.println("2. Suspend");
                System.out.println("3. Block");
                System.out.println("0. Go Back");

                int statusChoice = InputHelpers.readInt("Enter choice (0-3): ");

                switch (statusChoice) {
                    case 1 -> {
                        cardService.activateCard(cardId);
                        System.out.println("Card activated successfully.\n");
                    }
                    case 2 -> {
                        cardService.suspendCard(cardId);
                        System.out.println("Card suspended successfully.\n");
                    }
                    case 3 -> {
                        cardService.blockCard(cardId);
                        System.out.println("Card blocked successfully.\n");
                    }
                    case 0 -> {
                        return;
                    }
                    default -> {
                        System.out.println("Invalid choice. Please try again.");
                        continue;
                    }
                }
                break; // Exit the status update loop after a valid operation
            }

            break;
        }
    }

    private static void displayFraudAlerts() {
        List<Object[]> history = fraudService.getAllFraudHistory();

        System.out.println("\n============ Fraud Alerts ============\n");
        System.out.printf("%-3s %-7s %-8s %-15s %-27s %-11s %-50s%n",
                "ID", "Level", "CardID", "Client Name", "Client Email", "Phone", "Description");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------");

        for (Object[] record : history) {
            FraudAlert fraud = (FraudAlert) record[0];
            String name = (String) record[1];
            String email = (String) record[2];
            String phone = (String) record[3];

            System.out.printf("%-3d %-10s %-6d %-15s %-25s %-12s %-50s%n",
                    fraud.id(),
                    fraud.level(),
                    fraud.cardId(),
                    name,
                    email,
                    phone,
                    fraud.description());
        }
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }


}
