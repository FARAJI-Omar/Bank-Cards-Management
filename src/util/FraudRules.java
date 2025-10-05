package util;

import DAO.FraudAlertDAO;
import DAO.OperationDAO;
import entity.*;
import entity.enums.Level;
import entity.enums.Type;

import java.util.List;
import java.util.Optional;

public class FraudRules {
    private final FraudAlertDAO fraudAlertDAO;
    private final OperationDAO operationDAO;

    public FraudRules(FraudAlertDAO fraudAlertDAO, OperationDAO operationDAO) {
        this.fraudAlertDAO = fraudAlertDAO;
        this.operationDAO = operationDAO;
    }

    // Main entry point — called after each operation
    public void evaluate(Optional<Card> optCard, double amount, Type operationType, String location) {
        if (optCard.isEmpty()) {
            System.out.println("Fraud check skipped: card not found");
            return;
        }
        Card card = optCard.get();
        checkHighAmount(card, amount);
        checkRepetition(card.getId(), operationType, location);
    }

    // rule1: High amount compared to card limits/balance
    private void checkHighAmount(Card card, double amount) {
        double limit = 0;
        String description = "";
        Level level = null;

        if (card instanceof CreditCard credit) {
            limit = credit.getMonthlyLimit();
            description = "High amount detected on Credit Card";
        } else if (card instanceof DebitCard debit) {
            limit = debit.getDailyLimit();
            description = "High amount detected on Debit Card";
        } else if (card instanceof PrepaidCard prepaid) {
            limit = prepaid.getBalance();
            description = "High amount detected on Prepaid Card";
        }

        double ratio = amount / limit;

        if (ratio >= 0.9) {
            level = Level.Critical;
        } else if (ratio >= 0.7) {
            level = Level.Info;
        }

        if (level != null) {
            fraudAlertDAO.insert(new FraudAlert(
                    0,
                    description + " (" + (int)(ratio * 100) + "% of limit)",
                    level,
                    card.getId()
            ));
        }
    }

    // rule 2: Repeated purchases or online payments at same location
    private void checkRepetition(int cardId, Type operationType, String location) {
        if (operationType != Type.PURCHASE && operationType != Type.ONLINE_PAYMENT) {
            return;
        }

        // Get last operations for this card
        List<String> recentLocations = operationDAO.getRecentLocationsByCardId(cardId, 5);

        long sameLocationCount = recentLocations.stream()
                .filter(loc -> loc.equalsIgnoreCase(location))
                .count();

        if (sameLocationCount >= 3) {
            fraudAlertDAO.insert(new FraudAlert(
                    0,
                    "Repeated " + operationType + " detected at " + location,
                    Level.Warning,
                    cardId
            ));
        }
    }
}
