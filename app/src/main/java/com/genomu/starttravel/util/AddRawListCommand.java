package com.genomu.starttravel.util;

import java.util.List;

public class AddRawListCommand extends DBCommand {
    private String key;
    private List rawList;
    public AddRawListCommand(HanWen hanWen, String key, List rawList) {
        super(hanWen);
        this.key = key;
        this.rawList = rawList;
    }

    @Override
    void work() {
        hanWen.rawSet(key,rawList);
    }
}
