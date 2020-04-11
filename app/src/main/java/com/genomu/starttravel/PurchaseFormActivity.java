package com.genomu.starttravel;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.genomu.starttravel.travel_data.Travel;
import com.genomu.starttravel.util.AddOrderCommand;
import com.genomu.starttravel.util.DatabaseInvoker;
import com.genomu.starttravel.util.HanWen;
import com.google.firebase.firestore.auth.User;

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
        ActionBar ab = getSupportActionBar();
        ab.hide();
        findViews();
        setViews();

    }
    private int length;
    private void setViews() {
        amount = new int[]{1,0,0};
        final Travel travel =(Travel) getIntent().getSerializableExtra("travel");
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
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UID = UserAuth.getInstance().getUserUID();
                if (UID != "b07505019") {
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
            }
        });

    }

    private void setUpSeekBar(final TextView tag,final SeekBar bar) {
        bar.post(new Runnable() {
            @Override
            public void run() {
                length = (int)(bar.getWidth()*0.9f);
//                Log.d(TAG, "length>>"+length);
                final int tag_t = tag.getTop();
                final float sec = (float)length/(float)bar.getMax();
                tag.setVisibility(View.GONE);
                bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        tag.setX(seekBar.getLeft()+24+sec*progress);
//                        Log.d(TAG, "left>>"+seekBar.getLeft()+" , sec*progress>>"+sec*progress);
                        tag.setText(progress+"");
                        updateTotal();
                        total.setText(total_price+"");
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
        total_price = (int)((abar.getProgress()+kbar.getProgress()*0.8f+bbar.getProgress()*0.5f)*price);
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
