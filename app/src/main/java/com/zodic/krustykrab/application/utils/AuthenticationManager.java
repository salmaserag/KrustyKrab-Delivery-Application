/**
 * This class provides functionality for user authentication, including user registration and login.
 */
package com.zodic.krustykrab.application.utils;

import com.zodic.krustykrab.application.database.DAO.UserDAO;
import com.zodic.krustykrab.application.models.Role;
import com.zodic.krustykrab.application.models.User;

public class AuthenticationManager {
    private UserDAO userDAO;

    /**
     * Constructs an AuthenticationManager with the specified UserDAO.
     *
     * @param userDAO The UserDAO to be used for database operations.
     */
    public AuthenticationManager(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Registers a new user with the provided information.
     *
     * @param name     The name of the user.
     * @param email    The email of the user.
     * @param password The password of the user.
     * @param role     The role of the user.
     * @return The newly registered User object, or null if registration failed.
     */
    public User register(String name, String email, String password, Role role) {
        User newUser = new User(name, email, password, role);
        long userId = userDAO.addUser(newUser);

        if (userId != -1) {
            newUser.setId(userId);
            return newUser;
        } else {
            return null; // Registration failed
        }
    }

    /**
     * Authenticates a user with the provided email and password.
     *
     * @param email    The email of the user.
     * @param password The password of the user.
     * @return The role of the authenticated user, or null if login failed.
     */
    public Role login(String email, String password) {
        User user = userDAO.getUserByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            return user.getRole();
        } else {
            return null; // Login failed
        }
    }
}
