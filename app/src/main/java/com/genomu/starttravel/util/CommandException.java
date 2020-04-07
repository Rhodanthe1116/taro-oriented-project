package com.genomu.starttravel.util;

import androidx.annotation.Nullable;

public class CommandException extends Exception {
    private String msg;
    public static enum reasons {
        HW_NULL
    }
    public CommandException(reasons r){
        switch (r){
            case HW_NULL:
                msg ="HanWen cry for getting a null";
        }
    }

    @Nullable
    @Override
    public String getMessage() {
        return this.msg;
    }
}
