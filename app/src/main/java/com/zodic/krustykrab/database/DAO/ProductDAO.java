package com.zodic.krustykrab.database.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zodic.krustykrab.application.models.Category;
import com.zodic.krustykrab.application.models.Product;
import com.zodic.krustykrab.database.DatabaseConstants;
import com.zodic.krustykrab.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private static final String TAG = ProductDAO.class.getSimpleName();

    private SQLiteDatabase database;

    public ProductDAO(DatabaseHelper dbHelper) {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Add a new product to the database.
     *
     * @param product The Product object to add.
     * @return The ID of the newly added product.
     */
    public long addProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.COLUMN_PRODUCT_NAME, product.getName());
        values.put(DatabaseConstants.COLUMN_PRODUCT_INGREDIENTS, product.getIngredients());
        values.put(DatabaseConstants.COLUMN_PRODUCT_PRICE, product.getPrice());
        values.put(DatabaseConstants.COLUMN_PRODUCT_CATEGORY, product.getCategory().toString());
        values.put(DatabaseConstants.COLUMN_PRODUCT_IMAGE, product.getImagePath());

        long productId = database.insert(DatabaseConstants.TABLE_PRODUCTS, null, values);
        Log.d(TAG, "Added new product with ID: " + productId);

        return productId;
    }

    /**
     * Retrieve a product by its ID from the database.
     *
     * @param productId The ID of the product to retrieve.
     * @return The Product object with the specified ID, or null if not found.
     */
    @SuppressLint("Range")
    public Product getProductById(long productId) {
        Product product = null;

        String selection = DatabaseConstants.COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(productId) };

        Cursor cursor = database.query(
                DatabaseConstants.TABLE_PRODUCTS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_PRODUCT_NAME));
            String ingredients = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_PRODUCT_INGREDIENTS));
            double price = cursor.getDouble(cursor.getColumnIndex(DatabaseConstants.COLUMN_PRODUCT_PRICE));
            String categoryString = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_PRODUCT_CATEGORY));
            Category category = Category.valueOf(categoryString);
            String imagePath = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_PRODUCT_IMAGE));

            product = new Product(productId, name, ingredients, price, category, imagePath);
        }

        if (cursor != null) {
            cursor.close();
        }

        return product;
    }

    /**
     * Retrieve all products from the database.
     *
     * @return A list of all Product objects in the database.
     */
    @SuppressLint("Range")
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        Cursor cursor = database.query(
                DatabaseConstants.TABLE_PRODUCTS,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                long productId = cursor.getLong(cursor.getColumnIndex(DatabaseConstants.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_PRODUCT_NAME));
                String ingredients = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_PRODUCT_INGREDIENTS));
                double price = cursor.getDouble(cursor.getColumnIndex(DatabaseConstants.COLUMN_PRODUCT_PRICE));
                String categoryString = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_PRODUCT_CATEGORY));
                Category category = Category.valueOf(categoryString);
                String imagePath = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_PRODUCT_IMAGE));

                Product product = new Product(productId, name, ingredients, price, category, imagePath);
                products.add(product);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return products;
    }

    /**
     * Update an existing product in the database.
     *
     * @param product The Product object with updated data.
     * @return The number of rows affected (should be 1) or 0 if the update failed.
     */
    public int updateProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.COLUMN_PRODUCT_NAME, product.getName());
        values.put(DatabaseConstants.COLUMN_PRODUCT_INGREDIENTS, product.getIngredients());
        values.put(DatabaseConstants.COLUMN_PRODUCT_PRICE, product.getPrice());
        values.put(DatabaseConstants.COLUMN_PRODUCT_CATEGORY, product.getCategory().toString());
        values.put(DatabaseConstants.COLUMN_PRODUCT_IMAGE, product.getImagePath());

        String whereClause = DatabaseConstants.COLUMN_ID + " = ?";
        String[] whereArgs = { String.valueOf(product.getId()) };

        int rowsAffected = database.update(DatabaseConstants.TABLE_PRODUCTS, values, whereClause, whereArgs);
        if (rowsAffected > 0) {
            Log.d(TAG, "Updated product with ID: " + product.getId());
        } else {
            Log.d(TAG, "Failed to update product with ID: " + product.getId());
        }

        return rowsAffected;
    }

    /**
     * Delete a product from the database.
     *
     * @param product The Product object to delete.
     * @return The number of rows affected (should be 1) or 0 if the deletion failed.
     */
    public int deleteProduct(Product product) {
        String whereClause = DatabaseConstants.COLUMN_ID + " = ?";
        String[] whereArgs = { String.valueOf(product.getId()) };

        int rowsAffected = database.delete(DatabaseConstants.TABLE_PRODUCTS, whereClause, whereArgs);
        if (rowsAffected > 0) {
            Log.d(TAG, "Deleted product with ID: " + product.getId());
        } else {
            Log.d(TAG, "Failed to delete product with ID: " + product.getId());
        }

        return rowsAffected;
    }

    /**
     * Retrieve products by category from the database.
     *
     * @param category The category of the products to retrieve.
     * @return A list of Product objects with the specified category.
     */
    @SuppressLint("Range")
    public List<Product> getProductsByCategory(Category category) {
        List<Product> products = new ArrayList<>();

        String selection = DatabaseConstants.COLUMN_PRODUCT_CATEGORY + " = ?";
        String[] selectionArgs = { category.toString() };

        Cursor cursor = database.query(
                DatabaseConstants.TABLE_PRODUCTS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                long productId = cursor.getLong(cursor.getColumnIndex(DatabaseConstants.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_PRODUCT_NAME));
                String ingredients = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_PRODUCT_INGREDIENTS));
                double price = cursor.getDouble(cursor.getColumnIndex(DatabaseConstants.COLUMN_PRODUCT_PRICE));
                String categoryString = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_PRODUCT_CATEGORY));
                String imagePath = cursor.getString(cursor.getColumnIndex(DatabaseConstants.COLUMN_PRODUCT_IMAGE));
                Product product = new Product(productId, name, ingredients, price, category, imagePath);
                products.add(product);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return products;
    }

    /**
     * Get the total count of products in the database.
     *
     * @return The number of products in the database.
     */
    public int getProductCount() {
        int count = 0;

        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + DatabaseConstants.TABLE_PRODUCTS, null);
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }

        return count;
    }
}
