package com.genomu.starttravel.anim_manager;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

public class TickAnim {
    private ImageView round;
    private ImageView tick;
    private AnimatedVectorDrawable avd;

    public TickAnim(View round, View tick) {
        this.round = (ImageView) round;
        this.tick = (ImageView) tick;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void tickedAnimation() {
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
            AnimatedVectorDrawableCompat avdc = (AnimatedVectorDrawableCompat) drawable;
            avd.start();
        }else if(drawable instanceof AnimatedVectorDrawable){
            avd = (AnimatedVectorDrawable) drawable;
            avd.start();
        }
    }
}
