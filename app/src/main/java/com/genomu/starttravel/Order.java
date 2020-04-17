package com.genomu.starttravel;

import androidx.annotation.Nullable;

import com.genomu.starttravel.travel_data.Travel;

import java.io.Serializable;
import java.util.UUID;

public class Order implements Serializable {
    private Travel travel;
    private int adult;
    private int kid;
    private int baby;
    private String orderUID;

    @Override
    public boolean equals(@Nullable Object obj) {
        Order order = (Order) obj;
        if(order.orderUID.equals(orderUID)&&order.travel.getProduct_key().equals(travel.getProduct_key())){
            if(order.adult==adult&&order.kid==kid&&order.baby==baby){
                return true;
            }
        }
        return false;
    }

    public int getAdult() {
        return adult;
    }

    public void setAdult(int adult) {
        this.adult = adult;
    }

    public int getKid() {
        return kid;
    }

    public void setKid(int kid) {
        this.kid = kid;
    }

    public int getBaby() {
        return baby;
    }

    public void setBaby(int baby) {
        this.baby = baby;
    }

    public String getOrderUID() {
        return orderUID;
    }

    public Order(){
        travel=Travel.dummy;
    }

    public Order(Order order,int[] amount){
        this.travel = order.getTravel();
        this.adult = amount[0];
        this.kid = amount[1];
        this.baby = amount[2];
        this.orderUID = order.getOrderUID();
    }
    public Order(Travel travel, int adult, int kid, int baby) {
        this.travel = travel;
        this.adult = adult;
        this.kid = kid;
        this.baby = baby;
        this.orderUID = UUID.randomUUID().toString();
    }

    public Order(Travel travel) {
        this.travel = travel;
        this.adult = 0;
        this.kid = 0;
        this.baby = 0;
    }

    public Travel getTravel() {
        return travel;
    }

    public void setTravel(Travel travel) {
        this.travel = travel;
    }
}
