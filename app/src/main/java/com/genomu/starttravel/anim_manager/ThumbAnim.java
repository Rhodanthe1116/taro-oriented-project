package com.genomu.starttravel.anim_manager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class ThumbAnim {

    private int tag_t;
    private TextView tag;

    public ThumbAnim(int tag_t, TextView tag) {
        this.tag_t = tag_t;
        this.tag = tag;
    }

    public void fadeOut() {

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

    public void showUp(SeekBar seekBar, float sec) {
        tag.setVisibility(View.VISIBLE);
        tag.setX(seekBar.getLeft()+sec*seekBar.getProgress());
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
}
