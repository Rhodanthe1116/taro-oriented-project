package com.genomu.starttravel;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.genomu.starttravel.util.DBAspect;
import com.genomu.starttravel.util.DBDataObserver;
import com.genomu.starttravel.util.DBDataSubject;

public class LoadingDialog {
    private Activity activity;

    public AlertDialog getAlertDialog() {
        return alertDialog;
    }

    private AlertDialog alertDialog;

    public LoadingDialog(Activity activity) {
        this.activity = activity;
    }

    public void  startLoading(){
        DBDataSubject subject = new DBDataSubject() {
            @Override
            public void attach(DBDataObserver observer, DBAspect aspect) {

            }

            @Override
            public void detach(DBDataObserver observer) {

            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_loading,null));
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }

    public void dismissLoading(){
        alertDialog.dismiss();
    }

}
