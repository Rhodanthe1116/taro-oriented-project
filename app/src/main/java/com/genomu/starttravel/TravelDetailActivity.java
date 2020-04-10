package com.genomu.starttravel;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.genomu.starttravel.travel_data.Travel;

public class TravelDetailActivity extends AppCompatActivity {
    private TextView title;
    private TextView price;
    private TextView purchased;
    private TextView date;
    private TextView product_key;
    private TextView lower;
    private TextView upper;
    private Button go_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_detail);
        ActionBar ab = getSupportActionBar();
        ab.hide();
        setUpView();
        go_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TravelDetailActivity.this,"還未開放訂購",Toast.LENGTH_LONG).show();
            }
        });

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
        Travel travel = (Travel) getIntent().getSerializableExtra("travel");
        title.setText(travel.getTitle());
        price.setText("只要"+travel.getPrice()+"元");
        purchased.setText("已有 "+travel.getPurchased()+" 人訂購");
        date.setText(travel.getStart_date()+"~"+travel.getEnd_date());
        product_key.setText("產品代碼"+travel.getProduct_key());
        lower.setText("至少"+travel.getLower_bound()+"人");
        upper.setText("最多"+travel.getUpper_bound()+"人");
    }
}
