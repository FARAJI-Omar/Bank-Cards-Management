package entity;

import entity.enums.Status;

import java.time.LocalDateTime;

public sealed abstract class Card
    permits DebitCard, CreditCard, PrepaidCard {
    protected int id;
    protected String cardNumber;
    protected LocalDateTime expirationDate;
    protected Status status;
    protected int clientId;

    public Card(int id, String cardNumber, LocalDateTime expirationDate, Status status, int clientId) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.status = status;
        this.clientId = clientId;
    }

    // Getters
    public int getId() { return id; }
    public String getCardNumber() { return cardNumber; }
    public LocalDateTime getExpirationDate() { return expirationDate; }
    public Status getStatus() { return status; }
    public int getClientId() { return clientId; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public void setExpirationDate(LocalDateTime expirationDate) { this.expirationDate = expirationDate; }
    public void setStatus(Status status) { this.status = status; }
    public void setClientId(int clientId) { this.clientId = clientId;}
}
