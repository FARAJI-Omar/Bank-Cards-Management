package entity;

public sealed abstract class Card
    permits DebitCard, CreditCard, PrepaidCard {
}
