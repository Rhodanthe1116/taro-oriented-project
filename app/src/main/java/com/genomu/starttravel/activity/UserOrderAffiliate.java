package com.genomu.starttravel.activity;

import android.content.DialogInterface;
import android.os.Build;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.genomu.starttravel.LoadingDialog;
import com.genomu.starttravel.Order;
import com.genomu.starttravel.R;
import com.genomu.starttravel.anim_manager.SeekBarHandling;
import com.genomu.starttravel.util.CommandException;
import com.genomu.starttravel.util.DatabaseInvoker;
import com.genomu.starttravel.util.ExtirpateOrderCommand;
import com.genomu.starttravel.util.HanWen;

import java.util.HashMap;
import java.util.Map;

public class UserOrderAffiliate {

    private SeekBarHandling seekBarHandling;

    public UserOrderAffiliate(UserOrderActivity activity) {
        this.activity = activity;
    }

    private UserOrderActivity activity;

    public AlertDialog getReviseDialog(final Order order, View root) {
        SeekBar abar = root.findViewById(R.id.abar_revise);
        SeekBar kbar = root.findViewById(R.id.kbar_revise);
        SeekBar bbar = root.findViewById(R.id.bbar_revise);
        TextView atag = root.findViewById(R.id.atag_revise);
        TextView ktag = root.findViewById(R.id.ktag_revise);
        TextView btag = root.findViewById(R.id.btag_revise);
        final int[] amount = new int[3];
        amount[0] = order.getAdult();
        amount[1] = order.getKid();
        amount[2] = order.getBaby();
        Map<SeekBarHandling.typeId, SeekBar> barMap = new HashMap<>();
        barMap.put(SeekBarHandling.typeId.ADULT,abar);
        barMap.put(SeekBarHandling.typeId.KID, kbar);
        barMap.put(SeekBarHandling.typeId.BABY, bbar);
        Map<SeekBarHandling.typeId, TextView> textMap = new HashMap<>();
        textMap.put(SeekBarHandling.typeId.ADULT,atag);
        textMap.put(SeekBarHandling.typeId.KID, ktag);
        textMap.put(SeekBarHandling.typeId.BABY, btag);
        seekBarHandling = new SeekBarHandling(barMap, textMap,0,amount);
        return new AlertDialog.Builder(activity)
                .setTitle("修改訂單")
                .setMessage("調整訂單人數")
                .setNegativeButton(R.string.no_just_no,null)
                .setPositiveButton(R.string.confirm_btn, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {
                        try{
                            int[] amount = seekBarHandling.getAmount();
                            if(amount[0]>0){
                                activity.sendReviseRequest(order);
                            }else if(amount[1]!=0||amount[2]!=0){
                                throw new CommandException(CommandException.reasons.INPUT_INVALID,activity);
                            }else {
                                throw new CommandException(CommandException.reasons.INPUT_INVALID,activity);
                            }
                        }catch (CommandException e){
                            e.getExceptionDialog().show();
                        }
                    }
                })
                .setView(root)
                .create();
    }
    AlertDialog getCancelDialog(final Order order) {
        return new AlertDialog.Builder(activity)
                .setTitle("取消訂單")
                .setMessage("確定要取消訂單嗎")
                .setPositiveButton(R.string.confirm_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {
                        LoadingDialog loadingDialog = new LoadingDialog(activity);
                        DatabaseInvoker invoker = new DatabaseInvoker();
                        invoker.addCommand(new ExtirpateOrderCommand(new HanWen(),order,loadingDialog));
                        invoker.assignCommand();
                        activity.goCancel(loadingDialog);
                    }
                })
                .setNegativeButton(R.string.no_just_no,null)
                .create();
    }

    public int[] getAmount() {
        return seekBarHandling.getAmount();
    }
}
