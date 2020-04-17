package com.genomu.starttravel.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.genomu.starttravel.R;

public class CommandException extends Exception {
    private String msg;

    public AlertDialog getExceptionDialog() {
        return exceptionDialog;
    }

    public enum reasons {
        HW_NULL,INPUT_INVALID,NO_RESULT
    }

    private AlertDialog exceptionDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CommandException(reasons r, Activity activity){
        switch (r){
            case HW_NULL:
                msg ="HanWen cry for getting a null";
                break;
            case INPUT_INVALID:
                msg = "您的輸入有些問題，檢查是否有下列情形：\n◆ 使用者尚未登入\n◆ 沒有輸入任何數量\n◆ 成人數量為零\n◆ 超過出團上限";
                break;
            case NO_RESULT:
                msg = "沒有任何搜尋結果，您可以嘗試：\n◆ 檢查是否拼錯字\n◆ 將日期範圍擴大\n◆ 用建議的地區名稱進行搜尋";
                break;
        }
        if(activity!=null){
            this.exceptionDialog = new AlertDialog.Builder(activity)
                    .setTitle("出了點差錯")
                    .setMessage(msg)
                    .setNeutralButton("知道了",null)
                    .setView(R.layout.alert_view)
                    .create();
        }
    }

    @Nullable
    @Override
    public String getMessage() {
        return this.msg;
    }
}
