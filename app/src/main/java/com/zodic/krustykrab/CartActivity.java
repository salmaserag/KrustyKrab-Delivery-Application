package com.zodic.krustykrab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zodic.krustykrab.application.database.DAO.OrderDAO;
import com.zodic.krustykrab.application.database.DatabaseHelper;
import com.zodic.krustykrab.application.models.Category;
import com.zodic.krustykrab.application.models.Order;
import com.zodic.krustykrab.application.models.OrderProduct;
import com.zodic.krustykrab.application.models.Product;
import com.zodic.krustykrab.application.models.Role;
import com.zodic.krustykrab.application.models.User;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewlist;
    private OrderProduct orderProduct;
    private TextView totalfeeTxt, deliveryTxt, taxTxt, totalTxt, minusTxt, plusTxt, numItem, checkout;
    private double tax;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initview();
        initlist();
        bottonNavigation();
        calculateCard();
    }

    private void bottonNavigation() {
        LinearLayout homeBtn = findViewById(R.id.homeBtn);
        LinearLayout profileBtn = findViewById(R.id.profileBtn);
        LinearLayout cartBtn = findViewById(R.id.cartBtn);
        LinearLayout favBtn = findViewById(R.id.favBtn);
        LinearLayout settingBtn = findViewById(R.id.settingBtn);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartActivity.this, MainActivity.class));
            }
        });

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartActivity.this, CartActivity.class));
            }
        });
//

//        profileBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(CartActivity.this , ProfileActivity.class));
//            }
//        });
//

//        favBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(CartActivity.this , FavActivity.class));
//            }
//        });
//
//        settingBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(CartActivity.this , SettingActivity.class));
//            }
//        });

    }

    Order cartOrder;

    private void createFakeData() {
        List<OrderProduct> cartProducts = new ArrayList<>();

        cartOrder = new Order(new User("salma", "salmasss", "huvjmd", Role.CUSTOMER), cartProducts);
        Product Burger = new Product("burger", "ham and cheese", 150, Category.BURGER, "//");
        cartProducts.add(new OrderProduct(cartOrder, Burger, 4));
        Product Pizza = new Product("Pizza", "vegetables cheese", 100, Category.PIZZA, "//");
        cartProducts.add(new OrderProduct(cartOrder, Pizza, 2));


    }

    private void initlist() {
        createFakeData();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewlist.setHasFixedSize(true);
        recyclerViewlist.setLayoutManager(linearLayoutManager);
        CustomAdapter customAdaper = new CustomAdapter(cartOrder);
        recyclerViewlist.setAdapter(customAdaper);
        customAdaper.notifyDataSetChanged();
    }

    public void calculateCard() {
//
        double sum = 0.0;
        double tax = 0.1;
        double delivery = 10;

        List<OrderProduct> list = cartOrder.getOrderProducts();

        for (OrderProduct product : list) {
            double quantity = product.getQuantity();
            sum = sum + product.getProduct().getPrice() * quantity;
        }

        double taxAmount = tax * sum;


        totalfeeTxt.setText("$" + sum);
        taxTxt.setText("%10");
        deliveryTxt.setText("$" + delivery);
        totalTxt.setText("$" + (sum + taxAmount + delivery));
    }

    private void initview() {
        totalfeeTxt = findViewById(R.id.totalfeeTxt);
        deliveryTxt = findViewById(R.id.deliveryTxt);
        taxTxt = findViewById(R.id.taxtxt);
        totalTxt = findViewById(R.id.totalTxt);
        recyclerViewlist = findViewById(R.id.view);
        scrollView = findViewById(R.id.scrollView);
        checkout = findViewById(R.id.checkout);
        plusTxt = findViewById(R.id.plustxt);
        minusTxt = findViewById(R.id.minustxt);
        numItem = findViewById(R.id.numItemtxt);


        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderDAO orderDAO = new OrderDAO(new DatabaseHelper(CartActivity.this));
                orderDAO.addOrder(cartOrder);
            }
        });


    }

    private void example() {
//        List<OrderProduct> cartProducts = new ArrayList<>();
//        Order cartOrder = new Order(new User(),cartProducts);
//        Product Burger = new Product("burger","ham and cheese",150, Category.BURGER,"//");
//        cartProducts.add(new OrderProduct(cartOrder,Burger,10));
//        cartProducts.get(0).getProduct().getName();
//        OrderDAO orderDAO = new OrderDAO(new DatabaseHelper(this));
    }
}