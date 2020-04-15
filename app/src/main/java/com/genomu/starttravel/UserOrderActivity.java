package com.genomu.starttravel;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.genomu.starttravel.travel_data.Travel;
import com.genomu.starttravel.util.DatabaseInvoker;
import com.genomu.starttravel.util.ExtirpateOrderCommand;
import com.genomu.starttravel.util.HanWen;

public class UserOrderActivity extends AppCompatActivity {
    public final static int FUNC_USO = 7;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order);
        ActionBar ab = getSupportActionBar();
        ab.hide();
        findViews();
        setViews();
    }

    private void setViews() {
        final Order order = (Order) getIntent().getSerializableExtra("order");
        Travel travel = order.getTravel();
        title.setText(travel.getTitle());
        price.setText(travel.getPrice()+"元");
        UID.setText("訂單編號:"+order.getOrderUID());
        anum.setText("adult: "+order.getAdult());
        knum.setText("kid: "+order.getKid());
        bnum.setText("baby: "+order.getBaby());
        imageView.setImageResource(R.drawable.alert);
        setRevise(order);
        setCancel(order);
        long seed = TravelAdapter.getSeed(travel);
        TravelAdapter.parseCountryName(travel.getTravel_code(),this,imageView,seed);
    }

    private void setCancel(final Order order) {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(UserOrderActivity.this)
                        .setTitle("取消訂單")
                        .setMessage("確定要取消訂單嗎")
                        .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LoadingDialog loadingDialog = new LoadingDialog(UserOrderActivity.this);
                                DatabaseInvoker invoker = new DatabaseInvoker();
                                invoker.addCommand(new ExtirpateOrderCommand(new HanWen(),order.getOrderUID(),loadingDialog));
                                Log.d(TAG, "onClick: "+order.getOrderUID());
                                invoker.assignCommand();
                                final Intent intent = getIntent();
                                loadingDialog.startLoading();
                                loadingDialog.getAlertDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        setResult(RESULT_OK,intent);
                                        finish();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("deny",null)
                        .create();
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

    private void setRevise(final Order order) {
        revise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View root = View.inflate(getApplicationContext(),R.layout.activity_purchase_form,null);
                AlertDialog dialog = new AlertDialog.Builder(UserOrderActivity.this)
                        .setTitle("Revise order")
                        .setMessage("enter revised number below")
                        .setNegativeButton("deny",null)
                        .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setView(root)
                        .create();
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
