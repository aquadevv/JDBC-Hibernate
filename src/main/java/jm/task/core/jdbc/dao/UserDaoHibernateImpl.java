package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory = Util.getSessionFactory();
    private static final Logger logger = Logger.getLogger(UserDaoHibernateImpl.class.getName());

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
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.createSQLQuery(createUsersTableQuery).executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) transaction.rollback();
                logger.severe("Error creating users table: " + e.getMessage());
            }
        }
    }

    @Override
    public void dropUsersTable() {
        String dropUsersTableQuery = "DROP TABLE IF EXISTS users";
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.createSQLQuery(dropUsersTableQuery).executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) transaction.rollback();
                logger.severe("Error delete users table: " + e.getMessage());
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.save(new User(name, lastName, age));
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) transaction.rollback();
                logger.severe("Error saving user: " + e.getMessage());
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                User user = session.get(User.class, id);
                if (user != null) session.delete(user);
                else logger.log(Level.WARNING, "User with id {0} not found", id);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) transaction.rollback();
                logger.severe("Error delete user: " + e.getMessage());
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User", User.class).getResultList();
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.createQuery("DELETE FROM User").executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) transaction.rollback();
                logger.severe("Error cleaning user table: " + e.getMessage());
            }
        }
    }
}
