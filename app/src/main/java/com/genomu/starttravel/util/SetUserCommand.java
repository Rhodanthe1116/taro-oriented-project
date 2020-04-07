package com.genomu.starttravel.util;

import com.genomu.starttravel.Order;

import java.util.List;

public class SetUserCommand extends DBCommand {
    private String UID;
    private String name;
    private List<Order> orderList;
    public SetUserCommand(HanWen hanWen, String UID,String name, List<Order> orderList) {
        super(hanWen);
        this.UID = UID;
        this.name = name;
        this.orderList = orderList;
    }

    @Override
    void work() {
        hanWen.secureUser(UID);
        hanWen.sproutOnUser("name",name);
        hanWen.secureOrderListOnUser();
//        hanWen.sproutOnUser("orders",orderList);
    }
}
