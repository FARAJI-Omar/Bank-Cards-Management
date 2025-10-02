package entity;

import entity.enums.Status;

import java.time.LocalDateTime;
public final class CreditCard extends Card{
    private double monthlyLimit;
    private double interestRate;

    public  CreditCard(int id, String cardNumber, LocalDateTime expirationDate, Status status, int clientId, double monthlyLimit, double interestRate) {
        super(id, cardNumber, expirationDate, status, clientId);
        this.monthlyLimit = monthlyLimit;
        this.interestRate = interestRate;
    }

    // Getters
    public double getMonthlyLimit() {return monthlyLimit;}
    public double getInterestRate() {return interestRate;}

    // Setters
    public void setMonthlyLimit(double monthlyLimit) {this.monthlyLimit = monthlyLimit;}
    public void setInterestRate(double interestRate) {this.interestRate = interestRate;}
}
