package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("Ivan", "Ivanov", (byte) 5);
        userService.saveUser("Petr", "Petrov", (byte) 10);
        userService.saveUser("Sidor", "Sidorov", (byte) 15);
        userService.saveUser("Vasiliy", "Vasiliev", (byte) 20);
        userService.getAllUsers().forEach(user -> logger.info(user.toString()));
        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
