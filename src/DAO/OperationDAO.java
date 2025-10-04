package DAO;

import entity.CardOperation;
import entity.enums.Type;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OperationDAO {

    // add operation to the database
    public void insert(LocalDateTime operationDate, double amount, String type, String location, int cardId) {
        String SQLquery = "INSERT INTO cardoperation(operationDate, amount, type, location, cardId) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionDAO.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQLquery);
            preparedStatement.setTimestamp(1, Timestamp.valueOf(operationDate));
            preparedStatement.setDouble(2, amount);
            preparedStatement.setString(3, type);
            preparedStatement.setString(4, location);
            preparedStatement.setInt(5, cardId);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Operation inserted successfully");
            }
        } catch (SQLException e) {
            System.out.println("Error inserting operation: " + e.getMessage());
        }
    }

    // get card operations history
    public List<CardOperation> getCardOperationsHistory(int cardId) {
        List<CardOperation> operations = new ArrayList<>();
        String SQLquery = "SELECT * FROM cardoperation WHERE cardId = ? ORDER BY operationDate DESC";

        try (Connection connection = ConnectionDAO.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQLquery);
            preparedStatement.setInt(1, cardId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                CardOperation operation = new CardOperation(
                        resultSet.getInt("id"),
                        resultSet.getTimestamp("operationDate").toLocalDateTime(),
                        resultSet.getDouble("amount"),
                        Type.valueOf(resultSet.getString("type")),
                        resultSet.getString("location"),
                        resultSet.getInt("cardId")
                );
                operations.add(operation);
            }

        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
        return operations;
    }
}
