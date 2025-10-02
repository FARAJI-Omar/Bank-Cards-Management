package UI;

import util.InputHelpers;

public class Menu {
    public static void menu(){
        while (true){
            System.out.println("\n====== Welcome to the Bank Card Management System ======\n");
            System.out.println("1. Create a Client");
            System.out.println("2. Create a Card");
            System.out.println("3. Login");
            System.out.println("4. Make an Operation(Withdrawal, Purchase, Online Payment)");
            System.out.println("5. View a Card History");
            System.out.println("6. Fraud Analysis");
            System.out.println("7. Suspend/Block a Card");
            System.out.println("8. Exit");

            int choice = InputHelpers.readInt("\nPlease select an option (1-8): ");

            switch (choice){
                case 1:
                    System.out.println("Create a Client selected");
                case  2:
                    System.out.println("Create a Card selected");
                case 3:
                    System.out.println("Login selected");
                case 4:
                    System.out.println("Make an Operation selected");
                case 5:
                    System.out.println("View a Card History selected");
                case 6:
                    System.out.println("Fraud Analysis selected");
                case 7:
                    System.out.println("Suspend/Block a Card selected");
                case 8:
                    System.out.println("Exiting the system. Goodbye!");
                    System.exit(0);
            }
        }
    }
}
