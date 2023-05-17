package com.zodic.krustykrab.application.database.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.zodic.krustykrab.application.database.DatabaseConstants;
import com.zodic.krustykrab.application.database.DatabaseHelper;
import com.zodic.krustykrab.application.models.Order;
import com.zodic.krustykrab.application.models.OrderProduct;
import com.zodic.krustykrab.application.models.User;

import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    private static final String TAG = OrderDAO.class.getSimpleName();

    private SQLiteDatabase database;
    private UserDAO userDAO;
    private OrderProductDAO orderProductDAO;

    public OrderDAO(DatabaseHelper dbHelper) {
        database = dbHelper.getWritableDatabase();
        userDAO = new UserDAO(dbHelper);
        orderProductDAO = new OrderProductDAO(dbHelper);
    }

    /**
     * Add a new order to the database.
     *
     * @param order The Order object to add.
     * @return The ID of the newly added order.
     */
    public long addOrder(Order order) {
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.COLUMN_ORDER_USER_ID, order.getUser().getId());

        long orderId = database.insert(DatabaseConstants.TABLE_ORDERS, null, values);
        Log.d(TAG, "Added new order with ID: " + orderId);

        // Add order products to the OrderProductDAO
        for (OrderProduct orderProduct : order.getOrderProducts()) {
            orderProduct.setOrder(order);
            orderProductDAO.addOrderProduct(orderProduct);
        }

        return orderId;
    }

    /**
     * Retrieve an order by ID from the database.
     *
     * @param orderId The ID of the order to retrieve.
     * @return The Order object with the specified ID, or null if not found.
     */
    @SuppressLint("Range")
    public Order getOrderById(long orderId) {
        Order order = null;

        String selection = DatabaseConstants.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(orderId)};

        Cursor cursor = database.query(
                DatabaseConstants.TABLE_ORDERS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            long userId = cursor.getLong(cursor.getColumnIndex(DatabaseConstants.COLUMN_ORDER_USER_ID));
            User user = userDAO.getUserById(userId);
            List<OrderProduct> orderProducts = orderProductDAO.getOrderProductsForOrder(orderId);

            order = new Order(orderId, user, orderProducts);
        }

        if (cursor != null) {
            cursor.close();
        }

        return order;
    }

    /**
     * Retrieve all orders from the database.
     *
     * @return A list of all Order objects in the database.
     */
    @SuppressLint("Range")
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();

        Cursor cursor = database.query(
                DatabaseConstants.TABLE_ORDERS,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                long orderId = cursor.getLong(cursor.getColumnIndex(DatabaseConstants.COLUMN_ID));
                long userId = cursor.getLong(cursor.getColumnIndex(DatabaseConstants.COLUMN_ORDER_USER_ID));
                User user = userDAO.getUserById(userId);
                List<OrderProduct> orderProducts = orderProductDAO.getOrderProductsForOrder(orderId);

                Order order = new Order(orderId, user, orderProducts);
                orders.add(order);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return orders;
    }

    /**
     * Update an existing order in the database.
     *
     * @param order The Order object with updated information.
     * @return The number of rows affected by the update operation.
     */
    public int updateOrder(Order order) {
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.COLUMN_ORDER_USER_ID, order.getUser().getId());

        String whereClause = DatabaseConstants.COLUMN_ORDER_ID + " = ?";
        String[] whereArgs = {String.valueOf(order.getId())};

        int rowsAffected = database.update(DatabaseConstants.TABLE_ORDERS, values, whereClause, whereArgs);
        if (rowsAffected > 0) {
            Log.d(TAG, "Updated order with ID: " + order.getId());
        } else {
            Log.d(TAG, "Failed to update order with ID: " + order.getId());
        }

        return rowsAffected;
    }

    /**
     * Delete an order from the database.
     *
     * @param order The Order object to delete.
     * @return The number of rows affected by the delete operation.
     */
    public int deleteOrder(Order order) {
        String whereClause = DatabaseConstants.COLUMN_ORDER_ID + " = ?";
        String[] whereArgs = {String.valueOf(order.getId())};

        int rowsAffected = database.delete(DatabaseConstants.TABLE_ORDERS, whereClause, whereArgs);
        if (rowsAffected > 0) {
            Log.d(TAG, "Deleted order with ID: " + order.getId());
        } else {
            Log.d(TAG, "Failed to delete order with ID: " + order.getId());
        }

        return rowsAffected;
    }
}
