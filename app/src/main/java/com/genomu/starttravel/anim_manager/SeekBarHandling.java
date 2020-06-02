package com.genomu.starttravel.anim_manager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Build;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.Map;

public class SeekBarHandling {

    public enum typeId{ADULT,KID,BABY,TOTAL};
    private SeekBar abar;
    private SeekBar kbar;
    private SeekBar bbar;
    private TextView total;
    private int length;
    private int[] amount;
    private int total_price;
    private int price;
    public SeekBarHandling(Map<typeId,SeekBar> barMap,Map<typeId,TextView> textMap, int price,int[] amount){
        this.amount = amount;
        this.price = price;
        abar = barMap.get(typeId.ADULT);
        kbar = barMap.get(typeId.KID);
        bbar = barMap.get(typeId.BABY);
        TextView atag = textMap.get(typeId.ADULT);
        TextView ktag = textMap.get(typeId.KID);
        TextView btag = textMap.get(typeId.BABY);
        total = textMap.get(typeId.TOTAL);
        abar.setProgress(amount[0]);    kbar.setProgress(amount[1]);    bbar.setProgress(amount[2]);    bbar.setMax(amount[0]);
        assert atag != null;
        atag.setText(String.valueOf(amount[0]));
        assert ktag != null;
        ktag.setText(String.valueOf(amount[1]));
        assert btag != null;
        btag.setText(String.valueOf(amount[2]));
        if(total!=null){
            total.setText(String.valueOf(price));
        }
        setUpSeekBar(atag,abar);
        setUpSeekBar(ktag,kbar);
        setUpSeekBar(btag,bbar);
    }

    public int[] getAmount() {
        return amount;
    }
    private float baby_sec;
    private void setUpSeekBar(TextView tag,SeekBar bar) {
        bar.post(new RunnableBar(tag,bar));
    }

    private void updateTotal() {
        amount[0] = abar.getProgress(); amount[1] = kbar.getProgress(); amount[2] = bbar.getProgress();
        total_price = (int)((abar.getProgress()+kbar.getProgress()*0.7f+bbar.getProgress()*0.1f)*price);
    }

    private class RunnableBar implements Runnable, SeekBar.OnSeekBarChangeListener {

        TextView tag;
        SeekBar bar;
        private ThumbAnim thumbAnim;

        RunnableBar(TextView tag, SeekBar bar) {
            this.tag = tag;
            this.bar = bar;
        }

        @Override
        public void run() {
            length = (int)(abar.getWidth()*0.9f);
            baby_sec = length;
            int tag_t = tag.getTop();
            thumbAnim = new ThumbAnim(tag_t,tag);
            tag.setVisibility(View.GONE);
            bar.setOnSeekBarChangeListener(this);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            float sec = (float)length/(float)bar.getMax();
            if(seekBar==bbar&&fromUser){
                tag.setX(seekBar.getLeft()+24+baby_sec*progress);
            }else if(fromUser){
                tag.setX(seekBar.getLeft()+24+sec*progress);
            }

            tag.setText(progress+"");
            updateTotal();
            if(total!=null){
                total.setText(total_price+"");
            }
            if(seekBar==abar){
                bbar.setMax(progress);
                baby_sec = (float)length/(float)bbar.getMax();
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            float sec = (float)length/(float)bar.getMax();
            thumbAnim.showUp(seekBar,sec);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            thumbAnim.fadeOut();
        }
    }
}
