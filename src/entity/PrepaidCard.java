package entity;

import entity.enums.Status;

import java.time.LocalDateTime;

public final class PrepaidCard extends Card{
    private double balance;

    public  PrepaidCard(int id, String cardNumber, LocalDateTime expirationDate, Status status, int clientId, double balance) {
        super(id, cardNumber, expirationDate, status, clientId);
        this.balance = balance;
    }

    // Getters
    public double getBalance() {return balance;}

    // Setters
    public void setBalance(double balance) {this.balance = balance;}
}
