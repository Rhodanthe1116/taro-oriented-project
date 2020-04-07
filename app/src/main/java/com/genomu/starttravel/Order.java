package com.genomu.starttravel;

import com.genomu.starttravel.travel_data.Travel;

public class Order {
    private Travel travel;

    public Order(){
        travel=Travel.dummy;
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
