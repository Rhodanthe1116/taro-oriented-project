package com.genomu.starttravel;

import java.util.List;

public class UserInn {
    private String userUID;
    private String name;
    private int numberOfOrder;
    private List<Order> orderList;

    public UserInn(String userUID, String name, int numberOfOrder, List<Order> orderList) {
        this.userUID = userUID;
        this.name = name;
        this.numberOfOrder = numberOfOrder;
        this.orderList = orderList;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfOrder() {
        return numberOfOrder;
    }

    public void incrOrder(){
        this.numberOfOrder++;
    }

    public void decrOrder(){
        this.numberOfOrder--;
    }

    public void setNumberOfOrder(int numberOfOrder) {
        this.numberOfOrder = numberOfOrder;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void addOrderList(Order order) {
        this.orderList.add(order);
    }
}
