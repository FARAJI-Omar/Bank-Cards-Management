package entity;

import entity.enums.Type;

import java.time.LocalDateTime;

public record CardOperation(int id, LocalDateTime operationDate, double amount, Type type, String location, int cardId){};
