package entity;

import entity.enums.Status;

import java.time.LocalDateTime;

public final class DebitCard extends Card{
    private double dailyLimit;

    public  DebitCard(int id, String cardNumber, LocalDateTime expirationDate, Status status, int clientId, double dailyLimitLimit) {
        super(id, cardNumber, expirationDate, status, clientId);
        this.dailyLimit = dailyLimit;
    }

    // Getters
    public double getDailyLimit() {return dailyLimit;}

    // Setters
    public void setDailyLimit(double dailyLimit) {this.dailyLimit = dailyLimit;}
}
