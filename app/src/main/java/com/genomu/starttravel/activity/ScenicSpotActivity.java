package com.genomu.starttravel.activity;

import android.annotation.SuppressLint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.genomu.starttravel.R;
import com.genomu.starttravel.SpotDescribing;
import com.genomu.starttravel.util.OnOneOffClickListener;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ScenicSpotActivity extends AppCompatActivity {
    public final static int FUNC_SCS = 9;

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 50;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private boolean isScrolled = false;
    private ImageView down;
    private int which;

    private void startAnimation(View view,int resID){
        Animation animation = AnimationUtils.loadAnimation(this,resID);
        view.startAnimation(animation);
    }
    private void startAnimation(View view,int resID,long delay){
        Animation animation = AnimationUtils.loadAnimation(this,resID);
        animation.setStartOffset(delay);
        view.startAnimation(animation);
    }
    private View mControlsView;
    private ScrollView scrollView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    private Button let_go;
    private TextView paragraph;
    private TextView heading;
    private ImageView image;
    private ViewTreeObserver.OnScrollChangedListener onScrolled = new ViewTreeObserver.OnScrollChangedListener() {
        @Override
        public void onScrollChanged() {
            int scrollY = scrollView.getScrollY();
            if(scrollY>50&&!isScrolled){
                down.setAlpha(0.2f);
                startAnimation(heading,R.anim.scenic_slide_in);
                startAnimation(image,R.anim.scenic_slide_in);
                startAnimation(paragraph,R.anim.scenic_slide_in,800);
                startAnimation(let_go,R.anim.scenic_slide_in,1000);
                isScrolled = true;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scenic_spot);
        mVisible = true;
        setBackgroundGrad();
        findViews();
        ViewTreeObserver vto = scrollView.getViewTreeObserver();
        vto.addOnScrollChangedListener(onScrolled);

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                toggle();
                return false;
            }
        });
        initializeViews();
        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        setBtn();
    }

    private void setBtn() {
        Button btn = findViewById(R.id.dummy_button);
        btn.setText("返回首頁");
        btn.setOnClickListener(new OnGoingBackListener(false));
        let_go.setOnClickListener(new OnGoingBackListener(true));
    }
    private class OnGoingBackListener extends OnOneOffClickListener {
        private boolean isGoing;

        OnGoingBackListener(boolean isGoing) {
            this.isGoing = isGoing;
        }

        @Override
        public void onSingleClick(View v) {
            goBack(isGoing);
        }
    }

    private void initializeViews() {
        Intent intent = getIntent();
        String spots = intent.getStringExtra("spots");
        which = intent.getIntExtra("whichScene",0);
        SpotDescribing describing = new SpotDescribing(this, which);
        paragraph.setText(describing.getParagraph());
        image.setImageResource(describing.getImgResId());
        heading.setText(describing.getHeading());
        ((TextView)mContentView).setText(spots);
        startAnimation(down,R.anim.scenic_point_down);
        startAnimation(mContentView,R.anim.scenic_intro);
    }

    private void findViews() {
        let_go = findViewById(R.id.scenic_let_go_btn);
        scrollView = findViewById(R.id.scenic_scroll);
        down = findViewById(R.id.scenic_down);
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        paragraph = findViewById(R.id.scenic_paragraph);
        heading = findViewById(R.id.scenic_heading);
        image = findViewById(R.id.scenic_image);
        scrollView.setSmoothScrollingEnabled(true);
    }

    private void setBackgroundGrad() {
        ConstraintLayout background = findViewById(R.id.scenic_bg);
        AnimationDrawable gradient = (AnimationDrawable) background.getBackground();
        gradient.setEnterFadeDuration(2000);
        gradient.setExitFadeDuration(4000);
        gradient.start();
    }

    private void goBack(boolean isGoing) {
        final Intent intent = getIntent();
        intent.putExtra("whichGoing",which);
        setResult(isGoing?RESULT_OK:RESULT_CANCELED,intent);
        finish();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first

        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
