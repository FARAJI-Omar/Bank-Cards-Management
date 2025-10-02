package entity;

import entity.enums.Level;


public record FraudAlert(int id, String description, Level level, int cardId) {};
