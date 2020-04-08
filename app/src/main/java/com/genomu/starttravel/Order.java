package com.genomu.starttravel;

import com.genomu.starttravel.travel_data.Travel;

import java.util.UUID;

public class Order {
    private Travel travel;
    private int adult;
    private int kid;
    private int baby;
    private String orderUID;

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

    public Order(Travel travel, int adult, int kid, int baby) {
        this.travel = travel;
        this.adult = adult;
        this.kid = kid;
        this.baby = baby;
        this.orderUID = UUID.randomUUID().toString();
    }

    public Order(Travel travel) {
        this.travel = travel;
    }

    public Travel getTravel() {
        return travel;
    }

    public void setTravel(Travel travel) {
        this.travel = travel;
    }
}
