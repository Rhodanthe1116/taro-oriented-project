package com.genomu.starttravel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.genomu.starttravel.travel_data.Travel;
import com.genomu.starttravel.travel_data.TravelCode;
import com.genomu.starttravel.util.DatabaseInvoker;
import com.genomu.starttravel.util.HanWen;
import com.genomu.starttravel.util.LookForCodesCommand;

import static com.genomu.starttravel.PurchaseFormActivity.FUNC_PUR;

public class TravelDetailActivity extends AppCompatActivity {
    public final static int FUNC_TRA = 1;
    private TextView title;
    private TextView price;
    private TextView purchased;
    private TextView date;
    private TextView product_key;
    private TextView lower;
    private TextView upper;
    private Button go_btn;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_detail);
        ActionBar ab = getSupportActionBar();
        ab.hide();
        setUpView();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FUNC_PUR){
            if(resultCode == RESULT_OK){
                getIntent().putExtra("nav",R.id.navigation_users);
                setResult(RESULT_OK,getIntent());
                finish();
            }
        }
    }

    private void setUpView(){
        title = findViewById(R.id.title_detail);
        price = findViewById(R.id.price_detail);
        purchased = findViewById(R.id.purchased_detail);
        date = findViewById(R.id.date_detail);
        product_key =findViewById(R.id.product_key_detail);
        lower = findViewById(R.id.lower_detail);
        upper = findViewById(R.id.upper_detail);
        go_btn = findViewById(R.id.go_btn_detail);
        imageView = findViewById(R.id.image_detail);
        final Travel travel = (Travel) getIntent().getSerializableExtra("travel");
        title.setText(travel.getTitle());
        price.setText("只要"+travel.getPrice()+"元");
        purchased.setText("已有 "+travel.getPurchased()+" 人訂購");
        date.setText(travel.getStart_date()+"~"+travel.getEnd_date());
        product_key.setText("產品代碼"+travel.getProduct_key());
        lower.setText("至少"+travel.getLower_bound()+"人");
        upper.setText("最多"+travel.getUpper_bound()+"人");
        //waiting UI here
        imageView.setImageResource(R.drawable.alert);

        long seed = TravelAdapter.getSeed(travel);
        TravelAdapter.parseCountryName(travel.getTravel_code(),this,imageView,seed);
        go_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(travel.getTravel_code()!=433) {
                    Intent intent = new Intent(TravelDetailActivity.this, PurchaseFormActivity.class);
                    intent.putExtra("travel", travel);
                    startActivityForResult(intent, FUNC_PUR);
                }else{
                    new AlertDialog.Builder(TravelDetailActivity.this).setTitle("訂購錯誤").setMessage("目前無法前往武漢地區")
                            .setView(R.layout.alert_view)
                            .setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getIntent().putExtra("nav",R.id.navigation_search);
                                    setResult(RESULT_CANCELED,getIntent());
                                    finish();
                                }
                            }).show();
                }
            }
        });


    }
}
