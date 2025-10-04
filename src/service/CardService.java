package service;

import DAO.CardDAO;
import entity.Card;
import entity.enums.Status;

public class CardService {
    CardDAO cardDAO = new CardDAO();

    public CardService(CardDAO cardDAO) {
        this.cardDAO = cardDAO;
    }

    // add new card
    public void addNewCard(Card card) {
        try {
            cardDAO.insert(card);
            System.out.println("\nCard created successfully!");
        } catch (Exception e) {
            System.out.println("Error creating card: " + e.getMessage());
        }
    }

    // activate card
    public void activateCard(int cardId) {
        cardDAO.updateStatus(cardId, Status.Active);
    }

    // suspend card
    public void suspendCard(int cardId) {
        cardDAO.updateStatus(cardId, Status.Suspended);
    }

    // block card
    public void blockCard(int cardId) {
        cardDAO.updateStatus(cardId, Status.Blocked);
    }
}
