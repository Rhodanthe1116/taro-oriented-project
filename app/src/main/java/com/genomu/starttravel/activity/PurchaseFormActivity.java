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
import com.genomu.starttravel.anim_manager.SeekBarHandling;
import com.genomu.starttravel.travel_data.Travel;
import com.genomu.starttravel.util.AddOrderCommand;
import com.genomu.starttravel.util.CommandException;
import com.genomu.starttravel.util.DatabaseInvoker;
import com.genomu.starttravel.util.HanWen;
import com.genomu.starttravel.util.OnOneOffClickListener;

import java.util.HashMap;
import java.util.Map;

public class PurchaseFormActivity extends AppCompatActivity {

    public final static int FUNC_PUR = 2;
    private static final String TAG = PurchaseFormActivity.class.getSimpleName();
    private TextView title;
    private Button confirm;
    private Button cancel;
    private SeekBarHandling seekBarHandling;
    private Map<SeekBarHandling.typeId, SeekBar> barMap;
    private Map<SeekBarHandling.typeId, TextView> textMap;

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
                    .setMessage(getString(R.string.purchase_notice))
                    .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CheckBox checkBox = ((AlertDialog)dialog).findViewById(R.id.check_box_remind);
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
    private void setViews() {
        final Travel travel =(Travel) getIntent().getSerializableExtra("travel");
        title.setText(travel.getTitle());
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        seekBarHandling = new SeekBarHandling(barMap,textMap,travel.getPrice(),new int[]{1,0,0});
        confirm.setOnClickListener(new OnOneOffClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onSingleClick(View v) {
                String UID = UserAuth.getInstance().getUserUID();
                try {
                    if (!UID.equals("b07505019")) {
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
        int[] amount = seekBarHandling.getAmount();
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
        int[] amount = seekBarHandling.getAmount();
        int pur_amount = amount[0]+amount[1];
        int avai_amount = travel.getUpper_bound()-travel.getPurchased();
        if(pur_amount <= avai_amount){
            sendPurchaseRequest(UID, travel);
        }else{
            throw new CommandException(CommandException.reasons.INPUT_INVALID, PurchaseFormActivity.this);
        }
    }

    private void sendPurchaseRequest(String UID, Travel travel) {
        int[] amount = seekBarHandling.getAmount();
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


    private void findViews(){
        title = findViewById(R.id.title_purchase);
        confirm = findViewById(R.id.confirm_btn_purchase);
        cancel = findViewById(R.id.cancel_btn_purchase);
        barMap = new HashMap<>();
        barMap.put(SeekBarHandling.typeId.ADULT, (SeekBar) findViewById(R.id.seek_bar_adult));
        barMap.put(SeekBarHandling.typeId.KID, (SeekBar) findViewById(R.id.seek_bar_kid));
        barMap.put(SeekBarHandling.typeId.BABY, (SeekBar) findViewById(R.id.seek_bar_baby));
        textMap = new HashMap<>();
        textMap.put(SeekBarHandling.typeId.ADULT, (TextView) findViewById(R.id.adult_tag));
        textMap.put(SeekBarHandling.typeId.KID, (TextView) findViewById(R.id.kid_tag));
        textMap.put(SeekBarHandling.typeId.BABY, (TextView) findViewById(R.id.baby_tag));
        textMap.put(SeekBarHandling.typeId.TOTAL, (TextView) findViewById(R.id.total_purchase));
    }
}
