package com.zodic.krustykrab.database.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zodic.krustykrab.application.models.Order;
import com.zodic.krustykrab.application.models.OrderProduct;
import com.zodic.krustykrab.application.models.Product;
import com.zodic.krustykrab.database.DatabaseConstants;
import com.zodic.krustykrab.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class OrderProductDAO {
    private static final String TAG = OrderProductDAO.class.getSimpleName();

    private SQLiteDatabase database;
    private OrderDAO orderDAO;
    private ProductDAO productDAO;

    public OrderProductDAO(DatabaseHelper dbHelper) {
        database = dbHelper.getWritableDatabase();
        orderDAO = new OrderDAO(dbHelper);
        productDAO = new ProductDAO(dbHelper);
    }

    /**
     * Add a new order product to the database.
     *
     * @param orderProduct The OrderProduct object to add.
     * @return The ID of the newly added order product.
     */
    public long addOrderProduct(OrderProduct orderProduct) {
        // Create ContentValues object to store the order product data
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.COLUMN_ORDER_PRODUCT_ORDER_ID, orderProduct.getOrder().getId());
        values.put(DatabaseConstants.COLUMN_ORDER_PRODUCT_PRODUCT_ID, orderProduct.getProduct().getId());
        values.put(DatabaseConstants.COLUMN_ORDER_PRODUCT_QUANTITY, orderProduct.getQuantity());

        // Insert the order product data into the database
        long orderProductId = database.insert(DatabaseConstants.TABLE_ORDER_PRODUCTS, null, values);
        Log.d(TAG, "Added new order product with ID: " + orderProductId);

        return orderProductId;
    }

    /**
     * Retrieve an order product by its ID from the database.
     *
     * @param orderProductId The ID of the order product to retrieve.
     * @return The OrderProduct object with the specified ID, or null if not found.
     */
    @SuppressLint("Range")
    public OrderProduct getOrderProductById(long orderProductId) {
        OrderProduct orderProduct = null;

        // Define selection criteria
        String selection = DatabaseConstants.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(orderProductId)};

        // Query the database to retrieve the order product data
        Cursor cursor = database.query(
                DatabaseConstants.TABLE_ORDER_PRODUCTS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // Check if a result is found and create the OrderProduct object
        if (cursor != null && cursor.moveToFirst()) {
            long orderId = cursor.getLong(cursor.getColumnIndex(DatabaseConstants.COLUMN_ORDER_PRODUCT_ORDER_ID));
            Order order = orderDAO.getOrderById(orderId);
            long productId = cursor.getLong(cursor.getColumnIndex(DatabaseConstants.COLUMN_ORDER_PRODUCT_PRODUCT_ID));
            Product product = productDAO.getProductById(productId);
            int quantity = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_ORDER_PRODUCT_QUANTITY));

            orderProduct = new OrderProduct(orderProductId, order, product, quantity);
        }

        if (cursor != null) {
            cursor.close();
        }

        return orderProduct;
    }

    /**
     * Retrieve all order products from the database.
     *
     * @return A list of all OrderProduct objects in the database.
     */
    @SuppressLint("Range")
    public List<OrderProduct> getAllOrderProducts() {
        List<OrderProduct> orderProducts = new ArrayList<>();

        // Query the database to retrieve all order product data
        Cursor cursor = database.query(
                DatabaseConstants.TABLE_ORDER_PRODUCTS,
                null,
                null,
                null,
                null,
                null,
                null
        );

        // Iterate through the result set and create OrderProduct objects
        if (cursor != null && cursor.moveToFirst()) {
            do {
                long orderProductId = cursor.getLong(cursor.getColumnIndex(DatabaseConstants.COLUMN_ID));
                long orderId = cursor.getLong(cursor.getColumnIndex(DatabaseConstants.COLUMN_ORDER_PRODUCT_ORDER_ID));
                Order order = orderDAO.getOrderById(orderId);
                long productId = cursor.getLong(cursor.getColumnIndex(DatabaseConstants.COLUMN_ORDER_PRODUCT_PRODUCT_ID));
                Product product = productDAO.getProductById(productId);
                int quantity = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_ORDER_PRODUCT_QUANTITY));

                OrderProduct orderProduct = new OrderProduct(orderProductId, order, product, quantity);
                orderProducts.add(orderProduct);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return orderProducts;
    }

    /**
     * Update an existing order product in the database.
     *
     * @param orderProduct The OrderProduct object to update.
     * @return The number of rows affected by the update operation.
     */
    public int updateOrderProduct(OrderProduct orderProduct) {
        // Create ContentValues object to store the updated order product data
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.COLUMN_ORDER_PRODUCT_ORDER_ID, orderProduct.getOrder().getId());
        values.put(DatabaseConstants.COLUMN_ORDER_PRODUCT_PRODUCT_ID, orderProduct.getProduct().getId());
        values.put(DatabaseConstants.COLUMN_ORDER_PRODUCT_QUANTITY, orderProduct.getQuantity());

        // Define the update criteria
        String whereClause = DatabaseConstants.COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(orderProduct.getId())};

        // Update the order product data in the database
        int rowsAffected = database.update(DatabaseConstants.TABLE_ORDER_PRODUCTS, values, whereClause, whereArgs);
        if (rowsAffected > 0) {
            Log.d(TAG, "Updated order product with ID: " + orderProduct.getId());
        } else {
            Log.d(TAG, "Failed to update order product with ID: " + orderProduct.getId());
        }

        return rowsAffected;
    }

    /**
     * Delete an existing order product from the database.
     *
     * @param orderProduct The OrderProduct object to delete.
     * @return The number of rows affected by the delete operation.
     */
    public int deleteOrderProduct(OrderProduct orderProduct) {
        // Define the delete criteria
        String whereClause = DatabaseConstants.COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(orderProduct.getId())};

        // Delete the order product data from the database
        int rowsAffected = database.delete(DatabaseConstants.TABLE_ORDER_PRODUCTS, whereClause, whereArgs);
        if (rowsAffected > 0) {
            Log.d(TAG, "Deleted order product with ID: " + orderProduct.getId());
        } else {
            Log.d(TAG, "Failed to delete order product with ID: " + orderProduct.getId());
        }

        return rowsAffected;
    }

    /**
     * Retrieve all order products associated with a specific order from the database.
     *
     * @param orderId The ID of the order to retrieve order products for.
     * @return A list of OrderProduct objects associated with the specified order.
     */
    @SuppressLint("Range")
    public List<OrderProduct> getOrderProductsForOrder(long orderId) {
        List<OrderProduct> orderProducts = new ArrayList<>();

        // Define the selection criteria
        String selection = DatabaseConstants.COLUMN_ORDER_PRODUCT_ORDER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(orderId)};

        // Query the database to retrieve the order product data
        Cursor cursor = database.query(
                DatabaseConstants.TABLE_ORDER_PRODUCTS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // Iterate through the result set and create OrderProduct objects
        if (cursor != null && cursor.moveToFirst()) {
            do {
                long orderProductId = cursor.getLong(cursor.getColumnIndex(DatabaseConstants.COLUMN_ID));
                Order order = orderDAO.getOrderById(orderId);
                long productId = cursor.getLong(cursor.getColumnIndex(DatabaseConstants.COLUMN_ORDER_PRODUCT_PRODUCT_ID));
                Product product = productDAO.getProductById(productId);
                int quantity = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.COLUMN_ORDER_PRODUCT_QUANTITY));

                OrderProduct orderProduct = new OrderProduct(orderProductId, order, product, quantity);
                orderProducts.add(orderProduct);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return orderProducts;
    }
}
