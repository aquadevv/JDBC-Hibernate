package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoJDBCImpl implements UserDao {
    private static final Logger logger = Logger.getLogger(UserDaoJDBCImpl.class.getName());

    private Connection getConnection() {
        return Util.getConnectionToDatabase();
    }

    private void executeUpdate(String query, Object... parameters) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.severe("SQL operation failed. Error: " + e.getMessage());
        }
    }

    @Override
    public void createUsersTable() {
        String createUsersTableQuery = """
                CREATE TABLE IF NOT EXISTS users (
                    id BIGSERIAL PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    last_name VARCHAR(255) NOT NULL,
                    age SMALLINT NOT NULL
                );
                """;
        executeUpdate(createUsersTableQuery);
        logger.info("The users table has been successfully created.");
    }

    @Override
    public void dropUsersTable() {
        String dropUsersTableQuery = "DROP TABLE IF EXISTS users";
        executeUpdate(dropUsersTableQuery);
        logger.info("The users table was successfully deleted.");
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String saveUserQuery = "INSERT INTO users (name, last_name, age) VALUES (?, ?, ?)";
        executeUpdate(saveUserQuery, name, lastName, age);
        logger.log(Level.INFO, "User named - {0} added to the database", name);
    }

    @Override
    public void removeUserById(long id) {
        String removeUserQuery = "DELETE FROM users WHERE id = ?";
        executeUpdate(removeUserQuery, id);
        logger.log(Level.INFO, "User with ID {0} was successfully deleted.", id);
    }

    @Override
    public List<User> getAllUsers() {
        String getAllUsersQuery = "SELECT id, name, last_name, age FROM users";
        List<User> users = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(getAllUsersQuery)) {
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String lastName = resultSet.getString("last_name");
                byte age = resultSet.getByte("age");
                User user = new User(name, lastName, age);
                user.setId(id);
                users.add(user);
            }
        } catch (SQLException e) {
            logger.severe("Failed to get all users. Error: " + e.getMessage());
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        String cleanUsersTableQuery = "DELETE FROM users";
        executeUpdate(cleanUsersTableQuery);
        logger.info("All users were successfully deleted.");
    }
}
