package com.genomu.starttravel.util;

public abstract class DBCommand {
    protected HanWen hanWen;
    public  DBCommand(HanWen hanWen){
        this.hanWen = hanWen;
    }
    abstract void work();
}
