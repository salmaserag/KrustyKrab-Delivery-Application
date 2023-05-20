package com.zodic.krustykrab.application.models;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private long id;
    private User user;
    private List<OrderProduct> orderProducts;


    public Order(User user, List<OrderProduct> orderProducts) {
        this.user = user;
        this.orderProducts = orderProducts;

    }

    public Order(long id, User user, List<OrderProduct> orderProducts) {
        this.id = id;
        this.user = user;
        this.orderProducts = orderProducts;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public double calculateTotalPrice() {
        double totalPrice = 0;
        for (OrderProduct orderProduct : orderProducts) {
            totalPrice += orderProduct.getProduct().getPrice() * orderProduct.getQuantity();
        }
        return totalPrice;
    }

    public void addOrderProduct(OrderProduct orderProduct) {
        orderProducts.add(orderProduct);
    }

    public void removeOrderProduct(OrderProduct orderProduct) {
        orderProducts.remove(orderProduct);
    }


}