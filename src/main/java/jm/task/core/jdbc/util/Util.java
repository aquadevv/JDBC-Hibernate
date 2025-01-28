package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Util {
    private static final Logger logger = Logger.getLogger(Util.class.getName());

    private Util() {
    }

    public static Connection getConnectionToDatabase() {
        String url = PropertiesUtil.getProperty("db.url");
        String username = PropertiesUtil.getProperty("db.username");
        String password = PropertiesUtil.getProperty("db.password");
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            logger.severe("Failed to connect to the database. Error: " + e.getMessage());
        }
        return null;
    }

    public static SessionFactory getSessionFactory() {
        return new Configuration().addAnnotatedClass(User.class).buildSessionFactory();
    }
}
