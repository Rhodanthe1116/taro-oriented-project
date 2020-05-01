package com.genomu.starttravel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.genomu.starttravel.travel_data.Travel;
import com.genomu.starttravel.util.OnOneOffClickListener;
import com.genomu.starttravel.util.TravelStateOffice;

import java.text.ParseException;

import static com.genomu.starttravel.PurchaseFormActivity.FUNC_PUR;

public class TravelDetailActivity extends AppCompatActivity {
    public  final static  int RESULT_VIRUS = 433;
    public final static int FUNC_TRA = 1;
    private static final String TAG = TravelDetailActivity.class.getSimpleName();
    private TextView title;
    private TextView price;
    private TextView purchased;
    private TextView start_date;
    private TextView end_date;
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
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    private void setUpView(){
        title = findViewById(R.id.title_detail);
        price = findViewById(R.id.price_detail);
        purchased = findViewById(R.id.purchased_detail);
        start_date = findViewById(R.id.start_date_detail);
        end_date = findViewById(R.id.end_date_detail);
        product_key =findViewById(R.id.product_key_detail);
        lower = findViewById(R.id.lower_detail);
        upper = findViewById(R.id.upper_detail);
        go_btn = findViewById(R.id.go_btn_detail);
        imageView = findViewById(R.id.image_detail);
        final Travel travel = (Travel) getIntent().getSerializableExtra("travel");
        title.setText(travel.getTitle());
        price.setText(Integer.toString(travel.getPrice()));
        purchased.setText("已有 "+travel.getPurchased()+" 人訂購");
        start_date.setText(dateFormat(travel.getStart_date()));
        end_date.setText(dateFormat(travel.getEnd_date()));
        product_key.setText("產品代碼"+travel.getProduct_key());
        lower.setText(travel.getLower_bound()+"人成團");
        upper.setText(travel.getUpper_bound()+"人滿團");
        //waiting UI here
        imageView.setImageResource(R.drawable.alert);

        long seed = TravelAdapter.getSeed(travel);
        TravelAdapter.parseCountryName(travel.getTravel_code(),this,imageView,seed);
        try {
            TravelStateOffice office = new TravelStateOffice(travel);
            Log.d(TAG, "setUpView: "+office.getState());
            if(office.getState()>=TravelStateOffice.NOT_YET_START&&office.getGrouping()<TravelStateOffice.FULL){
                setGoBtn(travel);
            }else{
                go_btn.setText("行程無法預定");
                go_btn.setClickable(false);
                go_btn.setEnabled(false);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private String dateFormat(String date){
        String[] strings = date.split("-");
        return strings[0]+"\n"+strings[1]+"/"+strings[2];
    }
    private void setGoBtn(final Travel travel) {
        go_btn.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                if(travel.getTravel_code()!=433) {
                    Intent intent = new Intent(TravelDetailActivity.this, PurchaseFormActivity.class);
                    intent.putExtra("travel", travel);
                    startActivityForResult(intent, FUNC_PUR);
                }else{
                    dontGoToWuHan();
                }
            }
        });
    }

    private void dontGoToWuHan() {
        new AlertDialog.Builder(TravelDetailActivity.this).setTitle("訂購錯誤").setMessage("目前無法前往武漢地區")
                .setCancelable(false)
                .setView(R.layout.alert_view)
                .setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getIntent().putExtra("nav",R.id.navigation_search);
                        setResult(RESULT_VIRUS,getIntent());
                        finish();
                    }
                }).show();
    }
}
