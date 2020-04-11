package com.genomu.starttravel;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.genomu.starttravel.travel_data.Travel;

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
        setCancel();
        long seed = TravelAdapter.getSeed(travel);
        TravelAdapter.parseCountryName(travel.getTravel_code(),this,imageView,seed);
    }

    private void setCancel() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(UserOrderActivity.this)
                        .setTitle("Cancel order")
                        .setMessage("cancel failed")
                        .setView(R.layout.alert_view)
                        .setNegativeButton("...accept",null)
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
                View root = View.inflate(getApplicationContext(),R.layout.revise_order,null);
                final EditText edx_ad = root.findViewById(R.id.revise_adult_num);
                final EditText edx_kid = root.findViewById(R.id.revise_kid_num);
                final EditText edx_bb = root.findViewById(R.id.revise_baby_num);
                edx_ad.setText(order.getAdult()+"");
                edx_kid.setText(order.getKid()+"");
                edx_bb.setText(order.getBaby()+"");
                AlertDialog dialog = new AlertDialog.Builder(UserOrderActivity.this)
                        .setTitle("Revise order")
                        .setMessage("enter revised number below")
                        .setNegativeButton("deny",null)
                        .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                anum.setText("adult: "+edx_ad.getText().toString());
                                knum.setText("kid: "+edx_kid.getText().toString());
                                bnum.setText("baby: "+edx_bb.getText().toString());
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
