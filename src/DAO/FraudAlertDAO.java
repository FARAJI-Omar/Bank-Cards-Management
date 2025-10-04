package DAO;

import entity.FraudAlert;
import entity.enums.Level;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FraudAlertDAO {
    // insert new fraud alert in db
    public void insert(FraudAlert fraudAlert){
        String SQLquery = "INSERT INTO fraudalert(description, level, cardId) VALUES (?, ?, ?)";

        try (Connection connection = ConnectionDAO.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(SQLquery);

            preparedStatement.setString(1, fraudAlert.description());
            preparedStatement.setString(2, fraudAlert.level().name());
            preparedStatement.setInt(3, fraudAlert.cardId());

            preparedStatement.executeUpdate();
        } catch (SQLException e){
            System.out.println("SQL error: " + e.getMessage());
        } catch (Exception e){
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    // get fraud alerts by card id
    public List<FraudAlert> findByCardId(int cardId){
        List<FraudAlert> alerts = new ArrayList<>();
        String SQLquery = "SELECT * FROM fraudalert WHERE cardId = ?";

        try (Connection connection = ConnectionDAO.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(SQLquery);

            preparedStatement.setInt(1, cardId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                alerts.add(new FraudAlert(
                        resultSet.getInt("id"),
                        resultSet.getString("description"),
                        Level.valueOf(resultSet.getString("level")),
                        resultSet.getInt("cardId")
                ));
            }
        } catch (SQLException e){
            System.out.println("SQL error: " + e.getMessage());
        } catch (Exception e){
            System.out.println("Unexpected error: " + e.getMessage());
        } return alerts;
    }

    // get all fraud alerts
    public List<FraudAlert> findAll(){
        List<FraudAlert> alerts = new ArrayList<>();
        String SQLquery = "SELECT * FROM fraudalert";

        try (Connection connection = ConnectionDAO.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(SQLquery);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                alerts.add(new FraudAlert(
                        resultSet.getInt("id"),
                        resultSet.getString("description"),
                        Level.valueOf(resultSet.getString("level")),
                        resultSet.getInt("cardId")
                ));
            }
        } catch (SQLException e){
            System.out.println("SQL error: " + e.getMessage());
        } catch (Exception e){
            System.out.println("Unexpected error: " + e.getMessage());
        } return alerts;
    }
}
