package com.zodic.krustykrab.application.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "krustykrab.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createUsersTable(db);
        createProductsTable(db);
        createOrdersTable(db);
        createOrderProductsTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private void createUsersTable(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + DatabaseConstants.TABLE_USERS + " (" +
                DatabaseConstants.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseConstants.COLUMN_USER_NAME + " TEXT, " +
                DatabaseConstants.COLUMN_USER_EMAIL + " TEXT, " +
                DatabaseConstants.COLUMN_USER_PASSWORD + " TEXT, " +
                DatabaseConstants.COLUMN_USER_ROLE + " TEXT, " +
                DatabaseConstants.COLUMN_USER_ADDRESS + " TEXT" +
                ")";
        db.execSQL(createTableQuery);
    }

    private void createProductsTable(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + DatabaseConstants.TABLE_PRODUCTS + " (" +
                DatabaseConstants.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseConstants.COLUMN_PRODUCT_NAME + " TEXT, " +
                DatabaseConstants.COLUMN_PRODUCT_INGREDIENTS + " TEXT, " +
                DatabaseConstants.COLUMN_PRODUCT_PRICE + " REAL, " +
                DatabaseConstants.COLUMN_PRODUCT_CATEGORY + " TEXT, " +
                DatabaseConstants.COLUMN_PRODUCT_IMAGE + " TEXT" +
                ")";
        db.execSQL(createTableQuery);
    }

    private void createOrdersTable(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + DatabaseConstants.TABLE_ORDERS + " (" +
                DatabaseConstants.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseConstants.COLUMN_ORDER_USER_ID + " INTEGER, " +
                "FOREIGN KEY(" + DatabaseConstants.COLUMN_ORDER_USER_ID + ") REFERENCES " +
                DatabaseConstants.TABLE_USERS + "(" + DatabaseConstants.COLUMN_ID + ")" +
                ")";
        db.execSQL(createTableQuery);
    }

    private void createOrderProductsTable(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + DatabaseConstants.TABLE_ORDER_PRODUCTS + " (" +
                DatabaseConstants.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseConstants.COLUMN_ORDER_PRODUCT_ORDER_ID + " INTEGER, " +
                DatabaseConstants.COLUMN_ORDER_PRODUCT_PRODUCT_ID + " INTEGER, " +
                DatabaseConstants.COLUMN_ORDER_PRODUCT_QUANTITY + " INTEGER, " +
                "FOREIGN KEY(" + DatabaseConstants.COLUMN_ORDER_PRODUCT_ORDER_ID + ") REFERENCES " +
                DatabaseConstants.TABLE_ORDERS + "(" + DatabaseConstants.COLUMN_ID + ")," +
                "FOREIGN KEY(" + DatabaseConstants.COLUMN_ORDER_PRODUCT_PRODUCT_ID + ") REFERENCES " +
                DatabaseConstants.TABLE_PRODUCTS + "(" + DatabaseConstants.COLUMN_ID + ")" +
                ")";
        db.execSQL(createTableQuery);
    }
}
