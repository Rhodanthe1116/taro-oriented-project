package com.genomu.starttravel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import static com.genomu.starttravel.TravelDetailActivity.FUNC_TRA;
import static com.genomu.starttravel.TravelDetailActivity.RESULT_VIRUS;
import static com.genomu.starttravel.UserOrderActivity.FUNC_USO;
import static com.genomu.starttravel.UserOrderActivity.RESULT_REVISE;


public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public final static int FUNC_MAIN = 0;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static String search_content = "";
    private static boolean searched = false;
    private static NavController navController;
    public static boolean isStartBtn = true;
    private ImageView virus;
    private ImageView round;
    private ImageView tick;
    private AnimatedVectorDrawableCompat avdc;
    private AnimatedVectorDrawable avd;

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
        virus = findViewById(R.id.virus);
        tick = findViewById(R.id.tick_successful);
        round = findViewById(R.id.round_successful);
        ActionBar ab = getSupportActionBar();
        ab.hide();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FUNC_TRA){
            if(resultCode == RESULT_OK){
                tickedAnimation();
                Toast.makeText(this,"訂單成立",Toast.LENGTH_LONG).show();
                navGto(R.id.navigation_users);
            }else if(resultCode == RESULT_VIRUS){
                virus.setVisibility(View.VISIBLE);
                virus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final float scale = virus.getScaleX();
                        final float positionX = virus.getX();
                        final float positionY = virus.getY();
                        if(scale<8.0f){
                            virusGrow(scale,1.2f,500);
                            virusMove(positionX, positionY,505019);

                        }else {
                            virusGrow(scale,1/scale,1000);
                            touch_flag++;
                            virusExpand();
                        }
                    }
                });
            }
        }else if(requestCode == FUNC_USO){
            if(resultCode==RESULT_OK){
                tickedAnimation();
                Toast.makeText(this,"訂單已取消",Toast.LENGTH_LONG).show();
                navGto(R.id.navigation_users);
            }else if(resultCode==RESULT_REVISE){
                tickedAnimation();
                Toast.makeText(this,"訂單已修改",Toast.LENGTH_LONG).show();
                navGto(R.id.navigation_users);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void tickedAnimation() {
        tick.setVisibility(View.VISIBLE);
        round.setVisibility(View.VISIBLE);
        ValueAnimator alphaAnimator = ValueAnimator.ofFloat(0.0f,1.0f,1.0f,0.5f,0.0f);

        alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float curVal = (float)animation.getAnimatedValue();
                tick.setAlpha(curVal);
                round.setAlpha(curVal);
            }
        });
        alphaAnimator.setDuration(4200);
        alphaAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnimator.start();
        alphaAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                tick.setVisibility(View.GONE);
                round.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        Drawable drawable = tick.getDrawable();
        if(drawable instanceof AnimatedVectorDrawableCompat){
            avdc = (AnimatedVectorDrawableCompat)drawable;
            avd.start();
        }else if(drawable instanceof AnimatedVectorDrawable){
            avd = (AnimatedVectorDrawable) drawable;
            avd.start();
        }
    }

    private static long movingState = 0;
    private void virusMove(float positionX, float positionY, final long seed) {
        movingState = 0;
        final ValueAnimator animatorX = ValueAnimator.ofFloat(0,900);
        final ValueAnimator animatorY = ValueAnimator.ofFloat(0,1300);
        animatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float curVal = (float)animation.getAnimatedValue();
                virus.setX(curVal);

            }
        });
        animatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float curVal = (float)animation.getAnimatedValue();
                virus.setY(curVal);
            }
        });

        animatorX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                virus.setX(0);
                virus.setY(0);
            }



            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(movingState%4==0){
                    animatorY.start();
                }else{
                    animatorY.reverse();
                }
                movingState++;
                Toast.makeText(MainActivity.this,new Random(movingState).nextInt(Integer.MAX_VALUE)+"",Toast.LENGTH_SHORT).show();
            }
        });
        animatorY.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(movingState%4==3){
                    animatorX.start();
                }else{
                    animatorX.reverse();
                }
                movingState++;
                Toast.makeText(MainActivity.this,new Random(movingState).nextInt(Integer.MAX_VALUE)+"",Toast.LENGTH_SHORT).show();
            }
        });

        animatorX.setDuration(800);
        animatorY.setDuration(800);
        animatorX.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorY.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorX.start();
    }

    private static int touch_flag = 0;
    private void virusExpand(){
        switch (touch_flag){
            case 1:
                virus.setImageResource(R.drawable.covid_191);
                break;
            case 2:
                virus.setImageResource(R.drawable.covid_192);
                break;
            case 3:
                virus.setImageResource(R.drawable.covid_193);
                break;
            case 4:
                System.exit(0);
                break;
            default:
                virus.setImageResource(R.drawable.covid_19);
                break;
        }
    }


    private void virusGrow(float scale,float times,int duration) {
        ValueAnimator animator = ValueAnimator.ofFloat(scale,scale*times);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float curVal = (float)animation.getAnimatedValue();
                virus.setScaleX(curVal);
                virus.setScaleY(curVal);
            }
        });
        animator.setDuration(duration);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
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
            setUpStartDate(calendar, date, btn);
        }else{
            setUpEndDate(calendar, date, btn);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUpEndDate(Calendar calendar, String date, Button btn) {
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUpStartDate(Calendar calendar, String date, Button btn) {
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
