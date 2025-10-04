package DAO;

import entity.*;
import entity.enums.Status;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardDAO {

    // Insert card in db (different card types)
    public void insert(Card card) {
        String SQLquery = "INSERT INTO card(cardNumber, expirationDate, cardType, clientId, dailyLimit, monthlyLimit, interestRate, balance) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionDAO.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQLquery);

            // Set common parameters for all card types
            preparedStatement.setString(1, card.getCardNumber());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(card.getExpirationDate()));
            preparedStatement.setInt(4, card.getClientId());

            // Handle different card types with instanceof
            if (card instanceof CreditCard creditCard) {
                // CreditCard: insert card type and monthly_limit and interest_rate, others null
                preparedStatement.setString(3, "Credit");
                preparedStatement.setNull(5, Types.DOUBLE);
                preparedStatement.setDouble(6, creditCard.getMonthlyLimit());
                preparedStatement.setDouble(7, creditCard.getInterestRate());
                preparedStatement.setNull(8, Types.DOUBLE);

            } else if (card instanceof DebitCard debitCard) {
                // DebitCard: insert card type and daily_limit, others null
                preparedStatement.setString(3, "Debit");
                preparedStatement.setDouble(5, debitCard.getDailyLimit());
                preparedStatement.setNull(6, Types.DOUBLE);
                preparedStatement.setNull(7, Types.DOUBLE);
                preparedStatement.setNull(8, Types.DOUBLE);

            } else if (card instanceof PrepaidCard prepaidCard) {
                // PrepaidCard: insert card type and balance, others null
                preparedStatement.setString(3, "Prepaid");
                preparedStatement.setNull(5, Types.DOUBLE);
                preparedStatement.setNull(6, Types.DOUBLE);
                preparedStatement.setNull(7, Types.DOUBLE);
                preparedStatement.setDouble(8, prepaidCard.getBalance());

            } else {
                throw new IllegalArgumentException("Unknown card type");
            }

        } catch (SQLException e) {
            System.out.println("Error inserting card: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    // get Client's card by clientId
    public List<Card> getCardsByClientId(int clientId) {
        List<Card> cards = new ArrayList<>();
        String SQLquery = "SELECT * FROM card WHERE clientId = ?";

        try (Connection connection = ConnectionDAO.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQLquery);
            preparedStatement.setInt(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String cardType = resultSet.getString("cardType");
                Card card = switch (cardType) {
                    case "Credit" -> new CreditCard(
                            resultSet.getInt("id"),
                            resultSet.getString("cardNumber"),
                            resultSet.getTimestamp("expirationDate").toLocalDateTime(),
                            Status.valueOf(resultSet.getString("status")),
                            resultSet.getInt("clientId"),
                            resultSet.getDouble("monthlyLimit"),
                            resultSet.getDouble("interestRate")
                    );
                    case "Debit" -> new DebitCard(
                            resultSet.getInt("id"),
                            resultSet.getString("cardNumber"),
                            resultSet.getTimestamp("expirationDate").toLocalDateTime(),
                            Status.valueOf(resultSet.getString("status")),
                            resultSet.getInt("clientId"),
                            resultSet.getDouble("dailyLimit")
                    );
                    case "Prepaid" -> new PrepaidCard(
                            resultSet.getInt("id"),
                            resultSet.getString("cardNumber"),
                            resultSet.getTimestamp("expirationDate").toLocalDateTime(),
                            Status.valueOf(resultSet.getString("status")),
                            resultSet.getInt("clientId"),
                            resultSet.getDouble("balance")
                    );
                    default -> null;
                };

                if (card != null) {
                    cards.add(card);
                }
            }

        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
        return cards;
    }

    // get card by id
    public Optional<Card> findById(int id) {
        String SQLquery = "SELECT * FROM card WHERE id = ?";

        try (Connection connection = ConnectionDAO.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQLquery);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String cardType = resultSet.getString("cardType");
                Card card = switch (cardType) {
                    case "Credit" -> new CreditCard(
                            resultSet.getInt("id"),
                            resultSet.getString("cardNumber"),
                            resultSet.getTimestamp("expirationDate").toLocalDateTime(),
                            Status.valueOf(resultSet.getString("status")),
                            resultSet.getInt("clientId"),
                            resultSet.getDouble("monthlyLimit"),
                            resultSet.getDouble("interestRate")
                    );
                    case "Debit" -> new DebitCard(
                            resultSet.getInt("id"),
                            resultSet.getString("cardNumber"),
                            resultSet.getTimestamp("expirationDate").toLocalDateTime(),
                            Status.valueOf(resultSet.getString("status")),
                            resultSet.getInt("clientId"),
                            resultSet.getDouble("dailyLimit")
                    );
                    case "Prepaid" -> new PrepaidCard(
                            resultSet.getInt("id"),
                            resultSet.getString("cardNumber"),
                            resultSet.getTimestamp("expirationDate").toLocalDateTime(),
                            Status.valueOf(resultSet.getString("status")),
                            resultSet.getInt("clientId"),
                            resultSet.getDouble("balance")
                    );
                    default -> null;
                };
                return Optional.ofNullable(card);
            }

        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
        return Optional.empty();
    }

    // update card limits/balance
    public void update(Card card) {
        String SQLquery = "UPDATE card SET dailyLimit = ?, monthlyLimit = ?, balance = ? WHERE id = ?";

        try (Connection connection = ConnectionDAO.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQLquery);

            // Handle different card types with instanceof
            if (card instanceof CreditCard creditCard) {
                preparedStatement.setNull(1, Types.DOUBLE);
                preparedStatement.setDouble(2, creditCard.getMonthlyLimit());
                preparedStatement.setNull(3, Types.DOUBLE);

            } else if (card instanceof DebitCard debitCard) {
                preparedStatement.setDouble(1, debitCard.getDailyLimit());
                preparedStatement.setNull(2, Types.DOUBLE);
                preparedStatement.setNull(3, Types.DOUBLE);

            } else if (card instanceof PrepaidCard prepaidCard) {
                preparedStatement.setNull(1, Types.DOUBLE);
                preparedStatement.setNull(2, Types.DOUBLE);
                preparedStatement.setDouble(3, prepaidCard.getBalance());

            } else {
                throw new IllegalArgumentException("Unknown card type");
            }

            preparedStatement.setInt(4, card.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating card: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
}
