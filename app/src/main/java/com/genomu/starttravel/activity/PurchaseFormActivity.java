package com.genomu.starttravel.activity;

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
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import com.genomu.starttravel.LoadingDialog;
import com.genomu.starttravel.Order;
import com.genomu.starttravel.R;
import com.genomu.starttravel.UserAuth;
import com.genomu.starttravel.travel_data.Travel;
import com.genomu.starttravel.util.AddOrderCommand;
import com.genomu.starttravel.util.CommandException;
import com.genomu.starttravel.util.DatabaseInvoker;
import com.genomu.starttravel.util.HanWen;
import com.genomu.starttravel.util.OnOneOffClickListener;

public class PurchaseFormActivity extends AppCompatActivity {

    public final static int FUNC_PUR = 2;
    private static final String TAG = PurchaseFormActivity.class.getSimpleName();
    private TextView title;
    private Button confirm;
    private Button cancel;
    private SeekBar abar;
    private SeekBar kbar;
    private SeekBar bbar;
    private TextView atag;
    private TextView ktag;
    private TextView btag;
    private int total_price;
    private TextView total;
    private int price;
    private int[] amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_form);
        boolean remind = getSharedPreferences("StartTravel",MODE_PRIVATE)
                .getBoolean("remind_purchase",true);
        Log.d(TAG, "remind: "+remind);
        if(remind){
            new AlertDialog.Builder(this)
                    .setTitle("訂購須知")
                    .setView(R.layout.dialog_remind)
                    .setMessage("訂購前提醒您" +
                            "\n◆ 孩童與嬰兒的條件分別為:\n\r返國當天年滿2~15歲、2歲以下" +
                            "\n◆ 年滿15歲以成人價計費" +
                            "\n◆ 至少需要一個大人訂單才得成立" +
                            "\n◆ 一位嬰兒至少需要一位大人的陪同" +
                            "\n◆ 孩童享成人價7折、嬰兒則為1折")
                    .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CheckBox checkBox = ((AlertDialog)dialog).findViewById(R.id.check_box_remind);
                            Log.d(TAG, "isChecked: "+checkBox.isChecked());
                            getSharedPreferences("StartTravel",MODE_PRIVATE)
                                    .edit()
                                    .putBoolean("remind_purchase",!checkBox.isChecked())
                                    .apply();
                        }
                    })
                    .show();
        }
        findViews();
        setViews();

    }
    private int length;
    private void setViews() {
        amount = new int[]{1,0,0};
        final Travel travel =(Travel) getIntent().getSerializableExtra("travel");
        ktag.setText("0");
        btag.setText("0");
        title.setText(travel.getTitle());
        price = travel.getPrice();
        total.setText(price+"");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        setUpSeekBar(atag,abar);
        setUpSeekBar(ktag,kbar);
        setUpSeekBar(btag,bbar);
        confirm.setOnClickListener(new OnOneOffClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onSingleClick(View v) {
                String UID = UserAuth.getInstance().getUserUID();
                try {
                    if (UID != "b07505019") {
                        userIsLoggedPur(UID, travel);
                    }else{
                        throw new CommandException(CommandException.reasons.INPUT_INVALID,PurchaseFormActivity.this);
                    }
                }catch (CommandException e){
                    e.getExceptionDialog().show();
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void userIsLoggedPur(String UID, Travel travel) throws CommandException {
        if(amount[0]>0){
            adultAmountValidPur(UID, travel);
        }else if(amount[1]!=0||amount[2]!=0){
            throw new CommandException(CommandException.reasons.INPUT_INVALID,PurchaseFormActivity.this);
        }else {
            throw new CommandException(CommandException.reasons.INPUT_INVALID,PurchaseFormActivity.this);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void adultAmountValidPur(String UID, Travel travel) throws CommandException {
        int pur_amount = amount[0]+amount[1];
        int avai_amount = travel.getUpper_bound()-travel.getPurchased();
        if(pur_amount <= avai_amount){
            sendPurchaseRequest(UID, travel);
        }else{
            throw new CommandException(CommandException.reasons.INPUT_INVALID, PurchaseFormActivity.this);
        }
    }

    private void sendPurchaseRequest(String UID, Travel travel) {
        travel.setPurchased(travel.getPurchased()+amount[0]+amount[1]);
        DatabaseInvoker invoker = new DatabaseInvoker();
        LoadingDialog dialog = new LoadingDialog(PurchaseFormActivity.this);
        invoker.addCommand(new AddOrderCommand(new HanWen(), UID, new Order(travel,amount[0] , amount[1], amount[2]),dialog));
        invoker.assignCommand();
        final Intent intent = getIntent();
        dialog.startLoading();
        dialog.getAlertDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
    private float baby_sec;
    private void setUpSeekBar(final TextView tag,final SeekBar bar) {
        bar.post(new Runnable() {
            @Override
            public void run() {
                length = (int)(abar.getWidth()*0.9f);
                baby_sec = length;
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
                        updateTotal();
                        total.setText(total_price+"");
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

    private void updateTotal() {
        amount[0] = abar.getProgress(); amount[1] = kbar.getProgress(); amount[2] = bbar.getProgress();
        Log.d(TAG, "updateTotal: "+amount[0]+" , "+amount[1]*0.8f+" , "+amount[2]*0.5f);
        total_price = (int)((abar.getProgress()+kbar.getProgress()*0.7f+bbar.getProgress()*0.1f)*price);
        Log.d(TAG, "updateTotal: "+total_price);
    }

    private void findViews(){
        title = findViewById(R.id.title_purchase);
        confirm = findViewById(R.id.confirm_btn_purchase);
        cancel = findViewById(R.id.cancel_btn_purchase);
        abar = findViewById(R.id.seek_bar_adult);
        kbar = findViewById(R.id.seek_bar_kid);
        bbar = findViewById(R.id.seek_bar_baby);
        total = findViewById(R.id.total_purchase);
        atag = findViewById(R.id.adult_tag);
        ktag = findViewById(R.id.kid_tag);
        btag = findViewById(R.id.baby_tag);

    }
}
