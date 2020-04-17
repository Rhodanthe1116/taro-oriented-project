package com.genomu.starttravel;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.genomu.starttravel.travel_data.Travel;
import com.genomu.starttravel.util.AddOrderCommand;
import com.genomu.starttravel.util.CommandException;
import com.genomu.starttravel.util.DatabaseInvoker;
import com.genomu.starttravel.util.ExtirpateOrderCommand;
import com.genomu.starttravel.util.HanWen;
import com.genomu.starttravel.util.ReviseOrderCommand;

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
                                invoker.addCommand(new ExtirpateOrderCommand(new HanWen(),order,loadingDialog));
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
    private SeekBar abar;
    private SeekBar kbar;
    private SeekBar bbar;
    private TextView atag;
    private TextView ktag;
    private TextView btag;
    private int[] amount;
    private float baby_sec;
    private int length;
    private void setRevise(final Order order) {

        revise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstEnter = true;
                View root = View.inflate(getApplicationContext(),R.layout.revise_order,null);
                abar = root.findViewById(R.id.abar_revise);
                kbar = root.findViewById(R.id.kbar_revise);
                bbar = root.findViewById(R.id.bbar_revise);
                atag = root.findViewById(R.id.atag_revise);
                ktag = root.findViewById(R.id.ktag_revise);
                btag = root.findViewById(R.id.btag_revise);
                amount = new int[3];
                amount[0] = order.getAdult();   amount[1] = order.getKid();     amount[2] = order.getBaby();
                setUpSeekBar(atag,abar);
                setUpSeekBar(ktag,kbar);
                setUpSeekBar(btag,bbar);
                AlertDialog dialog = new AlertDialog.Builder(UserOrderActivity.this)
                        .setTitle("修改訂單")
                        .setMessage("調整訂單人數")
                        .setNegativeButton("deny",null)
                        .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try{
                                    if(amount[0]>0){
                                        sendReviseRequest(order);
                                    }else if(amount[1]!=0||amount[2]!=0){
                                        throw new CommandException(CommandException.reasons.INPUT_INVALID,UserOrderActivity.this);
                                    }else {
                                        throw new CommandException(CommandException.reasons.INPUT_INVALID,UserOrderActivity.this);
                                    }
                                }catch (CommandException e){
                                    e.getExceptionDialog().show();
                                }
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

    private void sendReviseRequest(Order order) {
        DatabaseInvoker invoker = new DatabaseInvoker();
        LoadingDialog loadingDialog = new LoadingDialog(UserOrderActivity.this);
        HanWen hanWen = new HanWen();
        invoker.addCommand(new ReviseOrderCommand(hanWen,this,order,loadingDialog,amount));
        invoker.assignCommand();
        final Intent intent = getIntent();
        loadingDialog.startLoading();
        loadingDialog.getAlertDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                setResult(RESULT_REVISE,intent);
                finish();
            }
        });
    }

    private static boolean firstEnter = true;
    private void setUpSeekBar(final TextView tag,final SeekBar bar) {
        bar.post(new Runnable() {
            @Override
            public void run() {
                if(firstEnter){
                    abar.setProgress(amount[0]);    kbar.setProgress(amount[1]);    bbar.setProgress(amount[2]);    bbar.setMax(amount[0]);
                    atag.setText(amount[0]+"");     ktag.setText(amount[1]+"");     btag.setText(amount[2]+"");
                    length = (int)(abar.getWidth()*0.9f);
                    baby_sec = (float) length/(float)amount[0];
                    btag.setX(bbar.getLeft()+24+baby_sec*amount[2]);
                    firstEnter = false;
                }
                final int tag_t = tag.getTop();
                final float sec = (float)length/(float)bar.getMax();
                tag.setVisibility(View.GONE);
                bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(seekBar==bbar&&fromUser){
                            tag.setX(seekBar.getLeft()+24+baby_sec*progress);
                        }else if(fromUser){
                            tag.setX(seekBar.getLeft()+24+sec*progress);
                        }

                        tag.setText(progress+"");
                        amount[0] = abar.getProgress(); amount[1] = kbar.getProgress(); amount[2] = bbar.getProgress();
                        if(seekBar==abar){
                            bbar.setMax(progress);
                            baby_sec = (float)length/(float)bbar.getMax();
                        }
                    }

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        showUp(tag_t,seekBar, tag, sec);
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        fadeOut();
                    }

                    private void fadeOut() {

                        ValueAnimator animatorY = ValueAnimator.ofInt(tag_t,tag_t+20);
                        ValueAnimator animatorA = ValueAnimator.ofInt(255,0);
                        animatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                int curVal = (Integer) animation.getAnimatedValue();
                                tag.layout(tag.getLeft(),curVal,tag.getRight(),curVal+tag.getHeight());
                            }
                        });
                        animatorA.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                int curVal = (Integer) animation.getAnimatedValue();
                                tag.getBackground().setAlpha(curVal);
                            }
                        });
                        animatorA.setDuration(500);
                        animatorY.setDuration(500);
                        animatorA.start();
                        animatorY.start();
                        animatorY.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                tag.setVisibility(View.GONE);
                            }
                        });
                    }
                });
            }
        });
    }

    private void showUp(int tag_t,SeekBar seekBar, final TextView tag, float sec) {
        tag.setVisibility(View.VISIBLE);
        tag.setX(seekBar.getLeft()+sec*seekBar.getProgress());
//        Log.d(TAG, "onStartTrackingTouch: "+"left>>"+seekBar.getLeft()+" , sec*progress>>"+sec*seekBar.getProgress());
        ValueAnimator animatorY = ValueAnimator.ofInt(tag_t,tag_t-20);
        ValueAnimator animatorA = ValueAnimator.ofInt(0,255);
        animatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int curVal = (Integer) animation.getAnimatedValue();
                tag.layout(tag.getLeft(),curVal,tag.getRight(),curVal+tag.getHeight());
            }
        });
        animatorA.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int curVal = (Integer) animation.getAnimatedValue();
                tag.getBackground().setAlpha(curVal);
            }
        });
        animatorA.setDuration(500);
        animatorY.setDuration(500);
        animatorA.start();
        animatorY.start();
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
