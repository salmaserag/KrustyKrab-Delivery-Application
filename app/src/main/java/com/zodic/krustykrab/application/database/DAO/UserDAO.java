package com.zodic.krustykrab.application.database.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.zodic.krustykrab.application.database.DatabaseConstants;
import com.zodic.krustykrab.application.database.DatabaseHelper;
import com.zodic.krustykrab.application.models.Role;
import com.zodic.krustykrab.application.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private static final String TAG = UserDAO.class.getSimpleName();

    private SQLiteDatabase database;

    public UserDAO(DatabaseHelper dbHelper) {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Add a new user to the database.
     *
     * @param user The User object to add.
     * @return The ID of the newly added user.
     */
    public long addUser(User user) {
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.COLUMN_USER_NAME, user.getName());
        values.put(DatabaseConstants.COLUMN_USER_EMAIL, user.getEmail());
        values.put(DatabaseConstants.COLUMN_USER_PASSWORD, user.getPassword());
        values.put(DatabaseConstants.COLUMN_USER_ROLE, user.getRole().toString());

        if (user.getRole() == Role.CUSTOMER) {
            // Set the address if the user is a customer
            values.put(DatabaseConstants.COLUMN_USER_ADDRESS, user.getAddress());
        } else {
            // Set an empty address for admin users
            values.put(DatabaseConstants.COLUMN_USER_ADDRESS, "");
        }

        long userId = database.insert(DatabaseConstants.TABLE_USERS, null, values);
        Log.d(TAG, "Added new user with ID: " + userId);

        return userId;
    }

    /**
     * Retrieve a user by ID from the database.
     *
     * @param userId The ID of the user to retrieve.
     * @return The User object with the specified ID, or null if not found.
     */
    @SuppressLint("Range")
    public User getUserById(long userId) {
        User user = null;

        String selection = DatabaseConstants.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        try (Cursor cursor = database.query(
                DatabaseConstants.TABLE_USERS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        )) {
            if (cursor != null && cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_USER_NAME));
                String email = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_USER_EMAIL));
                String password = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_USER_PASSWORD));
                String roleString = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_USER_ROLE));
                Role role = Role.valueOf(roleString);

                // Retrieve the address based on the user's role
                String address;
                if (role == Role.CUSTOMER) {
                    address = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_USER_ADDRESS));
                } else {
                    address = ""; // Set an empty address for admin users
                }

                user = new User(userId, name, email, password, role);
                user.setAddress(address);
            }
        }

        return user;
    }

    /**
     * Retrieve all users from the database.
     *
     * @return A list of all User objects in the database.
     */
    @SuppressLint("Range")
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        Cursor cursor = database.query(
                DatabaseConstants.TABLE_USERS,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                long userId = cursor.getLong(cursor.getColumnIndex(DatabaseConstants.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_USER_NAME));
                String email = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_USER_EMAIL));
                String password = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_USER_PASSWORD));
                String roleString = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_USER_ROLE));
                String address = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_USER_ADDRESS));
                Role role = Role.valueOf(roleString);

                User user = new User(userId, name, email, password, role);
                if (role == Role.CUSTOMER) {
                    user.setAddress(address);
                }
                users.add(user);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return users;
    }

    /**
     * Update an existing user in the database.
     *
     * @param user The User object with updated information.
     * @return The number of rows affected by the update operation.
     */
    public int updateUser(User user) {
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.COLUMN_USER_NAME, user.getName());
        values.put(DatabaseConstants.COLUMN_USER_EMAIL, user.getEmail());
        values.put(DatabaseConstants.COLUMN_USER_PASSWORD, user.getPassword());
        values.put(DatabaseConstants.COLUMN_USER_ROLE, user.getRole().toString());
        if (user.getRole() == Role.CUSTOMER) {
            values.put(DatabaseConstants.COLUMN_USER_ADDRESS, user.getAddress());
        } else {
            values.put(DatabaseConstants.COLUMN_USER_ADDRESS, "");
        }

        String whereClause = DatabaseConstants.COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(user.getId())};

        int rowsAffected = database.update(DatabaseConstants.TABLE_USERS, values, whereClause, whereArgs);
        if (rowsAffected > 0) {
            Log.d(TAG, "Updated user with ID: " + user.getId());
        } else {
            Log.d(TAG, "Failed to update user with ID: " + user.getId());
        }

        return rowsAffected;
    }

    /**
     * Delete a user from the database.
     *
     * @param user The User object to delete.
     * @return The number of rows affected by the delete operation.
     */
    public int deleteUser(User user) {
        String whereClause = DatabaseConstants.COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(user.getId())};

        int rowsAffected = database.delete(DatabaseConstants.TABLE_USERS, whereClause, whereArgs);
        if (rowsAffected > 0) {
            Log.d(TAG, "Deleted user with ID: " + user.getId());
        } else {
            Log.d(TAG, "Failed to delete user with ID: " + user.getId());
        }

        return rowsAffected;
    }

    /**
     * Retrieve a user by email from the database.
     *
     * @param email The email of the user to retrieve.
     * @return The User object with the specified email, or null if not found.
     */
    @SuppressLint("Range")
    public User getUserByEmail(String email) {
        User user = null;

        String selection = DatabaseConstants.COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {email};

        try (Cursor cursor = database.query(
                DatabaseConstants.TABLE_USERS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        )) {
            if (cursor != null && cursor.moveToFirst()) {
                long userId = cursor.getLong(cursor.getColumnIndex(DatabaseConstants.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_USER_NAME));
                String password = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_USER_PASSWORD));
                String roleString = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_USER_ROLE));
                Role role = Role.valueOf(roleString);

                // Retrieve the address based on the user's role
                String address;
                if (role == Role.CUSTOMER) {
                    address = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_USER_ADDRESS));
                } else {
                    address = ""; // Set an empty address for admin users
                }

                user = new User(userId, name, email, password, role);
                user.setAddress(address);
            }
        }

        return user;
    }


}
