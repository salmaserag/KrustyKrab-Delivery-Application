package com.zodic.krustykrab.application.models;


public class Product {
    private long id;
    private String name;
    private String ingredients;
    private double price;
    private Category category;
    private String imagePath;

    public Product(String name, String ingredients, double price, Category category, String imagePath) {
        this.name = name;
        this.ingredients = ingredients;
        this.price = price;
        this.category = category;
        this.imagePath = imagePath;
    }

    public Product(long id, String name, String ingredients, double price, Category category, String imagePath) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.price = price;
        this.category = category;
        this.imagePath = imagePath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}