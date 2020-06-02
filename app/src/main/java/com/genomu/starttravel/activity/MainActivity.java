package com.genomu.starttravel.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import genomu.fire_image_helper.ImagePermissionHelper;
import genomu.fire_image_helper.ImagePickingToken;

import com.genomu.starttravel.R;
import com.genomu.starttravel.anim_manager.TickAnim;
import com.genomu.starttravel.UserAuth;
import com.genomu.starttravel.anim_manager.VirusManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;

import static com.genomu.starttravel.activity.LoginActivity.FUNC_LIN;
import static com.genomu.starttravel.activity.ScenicSpotActivity.FUNC_SCS;
import static com.genomu.starttravel.activity.TravelDetailActivity.FUNC_TRA;
import static com.genomu.starttravel.activity.TravelDetailActivity.RESULT_VIRUS;
import static com.genomu.starttravel.activity.UserOrderActivity.FUNC_USO;
import static com.genomu.starttravel.activity.UserOrderActivity.RESULT_REVISE;


public class MainActivity extends ImagePickingToken implements DatePickerDialog.OnDateSetListener {

    public final static int FUNC_MAIN = 0;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static NavController navController;
    public static boolean isStartBtn = true;
    private VirusManager manager;
    private TickAnim anim;


    private static int whichGoing = -1;

    public static void resetWhichGoing() {
        MainActivity.whichGoing = -1;
    }

    public static int getWhichGoing() {
        return whichGoing;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setHelper();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "userInn status:"+ UserAuth.getInstance().getStatus());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        manager = new VirusManager(findViewById(R.id.virus));
        anim = new TickAnim(findViewById(R.id.round_successful),findViewById(R.id.tick_successful));

    }

    @Override
    public void onResolveImage(Bitmap image) {
        super.onResolveImage(image);
        ImageView imageView  = findViewById(R.id.image_users_logged);
        imageView.setImageBitmap(image);
    }

    @Override
    protected void setHelper() {
        helper = new ImagePermissionHelper(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FUNC_TRA){
            if(resultCode == RESULT_OK){
                anim.tickedAnimation();
                Toast.makeText(this,"訂單成立",Toast.LENGTH_LONG).show();
                navGto(R.id.navigation_users);
            }else if(resultCode == RESULT_VIRUS){
                manager.wakeUp();
            }
        }else if(requestCode == FUNC_USO){
            if(resultCode==RESULT_OK){
                anim.tickedAnimation();
                Toast.makeText(this,"訂單已取消",Toast.LENGTH_LONG).show();
                navGto(R.id.navigation_users);
            }else if(resultCode==RESULT_REVISE){
                anim.tickedAnimation();
                Toast.makeText(this,"訂單已修改",Toast.LENGTH_LONG).show();
                navGto(R.id.navigation_users);
            }
        }else if(requestCode == FUNC_LIN){
            if(resultCode==RESULT_OK){
                UserAuth.getInstance().setLogged(true);
                anim.tickedAnimation();
                Toast.makeText(this,"成功登入",Toast.LENGTH_LONG).show();
                navGto(R.id.navigation_users);
            }
        }else if(requestCode== FUNC_SCS){
            if(resultCode==RESULT_OK){
                whichGoing = data.getIntExtra("whichGoing",-1);

                navGto(R.id.navigation_search);
            }
        }
    }



    public static void navGto(int res_id){
        navController.navigate(res_id);
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
            setUpStartDate(calendar, date, btn);
        }else{
            setUpEndDate(calendar, date, btn);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUpEndDate(Calendar calendar, String date, Button btn) {
        Button end_btn = findViewById(R.id.end_date_btn);
        DateFormat format = DateFormat.getDateInstance(DateFormat.FULL);
        String start = btn.getText().toString();
        try {
            if(start.equals(getResources().getString(R.string.start_date_btn))){
                end_btn.setText(date);
            }else {
                if (calendar.getTime().after(format.parse(start))) {
                    end_btn.setText(date);
                } else {
                    alertKabo();
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUpStartDate(Calendar calendar, String date, Button btn) {
        Button end_btn = findViewById(R.id.end_date_btn);
        DateFormat format = DateFormat.getDateInstance(DateFormat.FULL);
        String end = end_btn.getText().toString();
        try {
            if(end.equals(getResources().getString(R.string.end_date_btn))){
                btn.setText(date);
            }else {
                if (calendar.getTime().before(format.parse(end))) {
                    btn.setText(date);
                } else {
                    alertKabo();
                }
            }
        } catch (ParseException e) {
            Log.w(TAG, "onDateSet: ", e);
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
