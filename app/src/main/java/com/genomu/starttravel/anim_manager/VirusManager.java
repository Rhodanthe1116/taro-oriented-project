package com.genomu.starttravel.anim_manager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.genomu.starttravel.R;

import java.util.Random;

public class VirusManager {
    private ImageView virus;
    private static int touch_flag = 0;
    private static long movingState = 0;

    public VirusManager(View virus) {
        this.virus = (ImageView) virus;
    }

    public void wakeUp(){
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
                Toast.makeText(virus.getContext(),new Random(movingState).nextInt(Integer.MAX_VALUE)+"",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(virus.getContext(),new Random(movingState).nextInt(Integer.MAX_VALUE)+"",Toast.LENGTH_SHORT).show();
            }
        });

        animatorX.setDuration(800);
        animatorY.setDuration(800);
        animatorX.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorY.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorX.start();
    }
}
