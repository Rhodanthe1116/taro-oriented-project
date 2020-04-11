package com.genomu.starttravel;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.genomu.starttravel.travel_data.JSONSaver;
import com.genomu.starttravel.travel_data.Travel;
import com.genomu.starttravel.travel_data.TravelCode;
import com.genomu.starttravel.travel_data.TravelCodeParser;
import com.genomu.starttravel.travel_data.TravelParser;
import com.genomu.starttravel.util.AddRawListCommand;
import com.genomu.starttravel.util.DatabaseInvoker;
import com.genomu.starttravel.util.HanWen;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.genomu.starttravel.TravelDetailActivity.FUNC_TRA;


public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public final static int FUNC_MAIN = 0;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static String search_content = "";
    private static boolean searched = false;
    private static NavController navController;
    public static boolean isStartBtn = true;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "userInn status:"+ UserAuth.getInstance().getStatus());
        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_users)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        ActionBar ab = getSupportActionBar();
        ab.hide();
//        int nav = getIntent().getIntExtra("nav",R.id.navigation_home);
//        navGto(nav);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FUNC_TRA){
            if(resultCode == RESULT_OK){
                int nav = data.getIntExtra("nav",R.id.navigation_home);
                navGto(nav);
            }else if(resultCode == RESULT_CANCELED){
                navGto(R.id.navigation_search);
            }
        }
    }

    public static void navGto(int res_id){
        navController.navigate(res_id);
    }

    public static String getSearch_content() {
        return search_content;
    }

    public static void setSearch_content(String search_content) {
        MainActivity.search_content = search_content;
        MainActivity.searched = true;
    }

    public static void setSearched(Boolean searched){
        MainActivity.searched = searched;
    }

    public static boolean getSearched(){
        return MainActivity.searched;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String date = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        Button btn = findViewById(R.id.start_date_search);
        if(isStartBtn){
            Button end_btn = findViewById(R.id.end_date_btn);
            DateFormat format = DateFormat.getDateInstance(DateFormat.FULL);
            try {
                if(end_btn.getText()==getResources().getString(R.string.end_date_btn)){
                    btn.setText(date);
                }else {
                    if (calendar.getTime().before(format.parse(end_btn.getText().toString()))) {
                        btn.setText(date);
                    } else {
                        alertKabo();
                    }
                }
            } catch (ParseException e) {
                Log.w(TAG, "onDateSet: ", e);
            }
        }else{
            Button end_btn = findViewById(R.id.end_date_btn);
            DateFormat format = DateFormat.getDateInstance(DateFormat.FULL);
            try {
                if(btn.getText()==getResources().getString(R.string.start_date_btn)){
                    end_btn.setText(date);
                }else {
                    if (calendar.getTime().after(format.parse(btn.getText().toString()))) {
                        end_btn.setText(date);
                    } else {
                        alertKabo();
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void alertKabo() {
        new AlertDialog.Builder(this)
                .setTitle("pick date error")
                .setMessage("原來是時光旅行")
                .setPositiveButton("ok",null)
                .setView(R.layout.alert_view)
                .show();
    }
}
