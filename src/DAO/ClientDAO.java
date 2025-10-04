package DAO;

import entity.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientDAO {
    // add new client to db
    public void insert(String name, String email, String phone) {
        String SQLquery = "INSERT INTO CLIENT(name, email, phone) VALUES (?,?,?)";

        try (Connection connection = ConnectionDAO.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQLquery);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Client inserted successfully");
            }
        } catch (SQLException e) {
            System.out.println("Error in inserting client:  " + e.getMessage());
        }
    }

    // get client by id
    public Optional<Client> findById(int id) {
        String SQLquery = "SELECT * FROM client WHERE id = ?";

        try (Connection connection = ConnectionDAO.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(SQLquery);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Client client = new Client(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone")
                );
                return Optional.of(client);
            }

        } catch (SQLException e){
            System.out.println("SQL Error: " + e.getMessage());
        } catch (Exception e){
            System.out.println("Unexpected error: " + e.getMessage());
        }
        return Optional.empty();
    }

    // get all clients
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        String SQLquery = "SELECT * FROM client";

        try (Connection connection = ConnectionDAO.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQLquery);

            while (resultSet.next()){
                Client client = new Client(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone")
                );
                clients.add(client);
            }

        } catch (SQLException e){
            System.out.println("SQL error: " + e.getMessage());
        } catch (Exception e){
            System.out.println("Unexpected error: " + e.getMessage());
        }
        return clients;
    }

}
