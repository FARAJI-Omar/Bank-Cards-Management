package service;

import DAO.CardDAO;
import DAO.OperationDAO;
import entity.*;
import entity.enums.Type;
import util.FraudRules;

import java.time.LocalDateTime;
import java.util.Optional;

public class OperationService {
    private final OperationDAO operationDAO;
    private final CardDAO cardDAO;
    private final FraudService fraudService;
    private final FraudRules fraudRules;

    public OperationService(OperationDAO operationDAO, CardDAO cardDAO, FraudService fraudService, FraudRules fraudRules) {
        this.operationDAO = operationDAO;
        this.cardDAO = cardDAO;
        this.fraudService = fraudService;
        this.fraudRules = fraudRules;
    }

    public void purchase(int cardId, double amount, String location) {
        executeOperation(cardId, amount, location, Type.PURCHASE);
        Optional<Card> card = cardDAO.findById(cardId);
        fraudRules.evaluate(card, amount, Type.PURCHASE, location);
    }

    public void withdraw(int cardId, double amount, String location) {
        executeOperation(cardId, amount, location, Type.WITHDRAWAL);
        Optional<Card> card = cardDAO.findById(cardId);
        fraudRules.evaluate(card, amount, Type.WITHDRAWAL, location);
    }

    public void onlinePurchase(int cardId, double amount, String location) {
        executeOperation(cardId, amount, location, Type.ONLINE_PAYMENT);
        Optional<Card> card = cardDAO.findById(cardId);
        fraudRules.evaluate(card, amount, Type.ONLINE_PAYMENT, location);
    }

    private void executeOperation(int cardId, double amount, String location, Type type) {
        Optional<Card> cardOpt = cardDAO.findById(cardId);
        if (cardOpt.isEmpty()) {
            System.out.println("Card not found.");
            return;
        }

        Card card = cardOpt.get();

        // Check if card is active
        if (card.getStatus() != entity.enums.Status.Active) {
            System.out.println("Operation denied. Card is not active.");
            return;
        }

        // Validate limits/balance
        if (!validateLimits(card, amount)) {
            System.out.println("Operation denied. Limit or balance exceeded.");
            return;
        }

        // Insert operation
        operationDAO.insert(LocalDateTime.now(), amount, type.name(), location, cardId);
        System.out.println(type.name() + " operation successful.");

        // Update card limits/balance
        updateCardLimits(card, amount);
    }

    private boolean validateLimits(Card card, double amount) {
        if (card instanceof DebitCard debitCard) {
            return amount <= debitCard.getDailyLimit();
        } else if (card instanceof CreditCard creditCard) {
            return amount <= creditCard.getMonthlyLimit();
        } else if (card instanceof PrepaidCard prepaidCard) {
            return amount <= prepaidCard.getBalance();
        }
        return false;
    }

    private void updateCardLimits(Card card, double amount) {
        if (card instanceof DebitCard debitCard) {
            debitCard.setDailyLimit(debitCard.getDailyLimit() - amount);
        } else if (card instanceof CreditCard creditCard) {
            creditCard.setMonthlyLimit(creditCard.getMonthlyLimit() - amount);
        } else if (card instanceof PrepaidCard prepaidCard) {
            prepaidCard.setBalance(prepaidCard.getBalance() - amount);
        }
        // Update the database with new limits/balance
        cardDAO.update(card);
    }

}
