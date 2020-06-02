package com.genomu.starttravel.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.genomu.starttravel.LoadingDialog;
import com.genomu.starttravel.Order;
import com.genomu.starttravel.R;
import com.genomu.starttravel.TravelAdapter;
import com.genomu.starttravel.travel_data.Travel;
import com.genomu.starttravel.util.DatabaseInvoker;
import com.genomu.starttravel.util.HanWen;
import com.genomu.starttravel.util.OnOneOffClickListener;
import com.genomu.starttravel.util.ReviseOrderCommand;
import com.genomu.starttravel.util.TravelStateOffice;

import java.text.ParseException;

public class UserOrderActivity extends AppCompatActivity {
    public final static int FUNC_USO = 7;
    public final static int RESULT_REVISE = 5;
    private static final String TAG = UserOrderActivity.class.getSimpleName();
    private TextView title;
    private TextView price;
    private TextView UID;
    private Button revise;
    private Button cancel;
    private TextView anum;
    private TextView knum;
    private TextView bnum;
    private ImageView imageView;
    private UserOrderAffiliate affiliate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order);
        findViews();
        setViews();
    }

    private  String getPurchase(int resID,int amount){
        return concatWord(resID,Integer.toString(amount));
    }
    private String concatWord(int resID, String str){
        return getString(resID).concat(" : "+str);
    }

    private void setViews() {
        final Order order = (Order) getIntent().getSerializableExtra("order");
        assert order != null;
        Travel travel = order.getTravel();
        title.setText(travel.getTitle());
        String strPrice = (travel.getPrice())+"元";
        price.setText(strPrice);
        UID.setText(concatWord(R.string.uid_from_uid,order.getOrderUID()));
        anum.setText(getPurchase(R.string.adult_purchase,order.getAdult()));
        knum.setText(getPurchase(R.string.kid_purchase,order.getKid()));
        bnum.setText(getPurchase(R.string.baby_purchase,order.getBaby()));
        imageView.setImageResource(R.drawable.alert);
        setBtn(order);
        long seed = TravelAdapter.getSeed(travel);
        TravelAdapter.parseCountryName(travel.getTravel_code(),this,imageView,seed);
    }


    private void clickBtn(final Order order, final boolean isCancel) {
        Button btn = (isCancel)?cancel:revise;
        btn.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                affiliate = new UserOrderAffiliate(UserOrderActivity.this);
                View root = View.inflate(getApplicationContext(),R.layout.revise_order,null);
                AlertDialog dialog = isCancel?affiliate.getCancelDialog(order): affiliate.getReviseDialog(order,root);
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        AlertDialog alertDialog = (AlertDialog) dialog;
                        Button button = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                        button.setTextColor(getResources().getColor(R.color.negative));
                    }
                });
                dialog.show();
            }
        });
    }

    private void setBtn(final Order order) {
        try {
            TravelStateOffice office = new TravelStateOffice(order.getTravel());
            if(office.getState()==TravelStateOffice.CAN_BE_MODIFIED){
                clickBtn(order,false);
                clickBtn(order,true);
            }else {
                revise.setText("無法修改");
                revise.setClickable(false);
                revise.setEnabled(false);
                revise.setBackgroundColor(Color.LTGRAY);
                cancel.setText("無法取消");
                cancel.setClickable(false);
                cancel.setEnabled(false);
                cancel.setBackgroundColor(Color.LTGRAY);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    void goCancel(LoadingDialog loadingDialog) {
        leaving(loadingDialog, RESULT_OK);
    }

    void sendReviseRequest(Order order) {
        DatabaseInvoker invoker = new DatabaseInvoker();
        LoadingDialog loadingDialog = new LoadingDialog(UserOrderActivity.this);
        HanWen hanWen = new HanWen();
        int[] amount = affiliate.getAmount();
        invoker.addCommand(new ReviseOrderCommand(hanWen,this,order,loadingDialog,amount));
        invoker.assignCommand();
        leaving(loadingDialog, RESULT_REVISE);
    }

    private void leaving(LoadingDialog loadingDialog, final int resultCode) {
        final Intent intent = getIntent();
        loadingDialog.startLoading();
        loadingDialog.getAlertDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                setResult(resultCode, intent);
                finish();
            }
        });
    }


    private void findViews(){
        title = findViewById(R.id.title_user_order);
        price = findViewById(R.id.price_user_order);
        UID = findViewById(R.id.UID_user_order);
        revise = findViewById(R.id.revise_btn_user_order);
        cancel = findViewById(R.id.cancel_btn_user_order);
        anum = findViewById(R.id.anum_user_order);
        knum = findViewById(R.id.knum_user_order);
        bnum = findViewById(R.id.bnum_user_order);
        imageView = findViewById(R.id.image_user_order);
    }
}
