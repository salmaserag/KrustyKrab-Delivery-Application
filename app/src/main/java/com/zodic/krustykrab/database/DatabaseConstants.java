package com.zodic.krustykrab.database;

public class DatabaseConstants {
    // Table names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_PRODUCTS = "products";
    public static final String TABLE_ORDERS = "orders";
    public static final String TABLE_ORDER_PRODUCTS = "order_products";

    // Common column names
    public static final String COLUMN_ID = "id";

    // Users table column names
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_ROLE = "role";
    public static final String COLUMN_USER_ADDRESS = "address";


    // Products table column names
    public static final String COLUMN_PRODUCT_NAME = "name";
    public static final String COLUMN_PRODUCT_INGREDIENTS = "ingredients";
    public static final String COLUMN_PRODUCT_PRICE = "price";
    public static final String COLUMN_PRODUCT_CATEGORY = "category";
    public static final String COLUMN_PRODUCT_IMAGE = "image";

    // Orders table column names
    public static final String COLUMN_ORDER_USER_ID = "userId";

    // Order products table column names
    public static final String COLUMN_ORDER_PRODUCT_ORDER_ID = "orderId";
    public static final String COLUMN_ORDER_PRODUCT_PRODUCT_ID = "productId";
    public static final String COLUMN_ORDER_PRODUCT_QUANTITY = "quantity";
}
