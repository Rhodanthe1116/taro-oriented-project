package com.genomu.starttravel.util;

import com.genomu.starttravel.Order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String UID;
    private String name;
    private List<Order> orders;

    public User(){
        this.UID = "b07505019";
        this.name = "User";
        this.orders = new ArrayList<>();
    }
    public User(String UID, String name, List<Order> orders) {
        this.UID = UID;
        this.name = name;
        this.orders = orders;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
