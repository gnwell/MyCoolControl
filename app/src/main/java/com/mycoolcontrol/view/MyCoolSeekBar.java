package com.mycoolcontrol.view;

import android.content.Context;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import com.mycoolcontrol.R;

import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;

/**
 * Date : 2019-01-28 10:08
 * Author : Gengbaojin
 * Comment :
 */
public class MyCoolSeekBar extends FrameLayout {

    private static final String TAG = "MyCoolSeekBar";
    private static final int SEEK_BAR_MAX = 500;
    private static final int MSG_UPDATE_PROGRESS = 0;

    private Context myContext;
    private Handler myUIHandler;

    private SeekBar mySeekBar;
    private FrameLayout myEffectViews;
    private View myCurEffectView;

    private Timer myTimer;
    private TimerTask myTimerTask;
    private long mDurationMSec;


    public MyCoolSeekBar(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MyCoolSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyCoolSeekBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setDurationMSec(long durationMSec){
        mDurationMSec = durationMSec;
    }

    public long getDurationMSec(){
        return mDurationMSec;
    }

    public void start(){
        startTimer();
    }

    public void stop(){
        stopTimer();
    }

    public MyCoolSeekBar setEffectColor(int color){
        initAndAddEffectView(color);
        return this;
    }

    public int getProgress(){
        return mySeekBar.getProgress();
    }

    public long getCurrentPosMSec(){
        return mySeekBar.getProgress() * mDurationMSec / SEEK_BAR_MAX;
    }

    public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener listener){
        mySeekBar.setOnSeekBarChangeListener(listener);
    }

    private void init(Context context){
        myContext = context;
        inflate(context, R.layout.cool_seek_bar, this);

        myEffectViews = findViewById(R.id.myEffectViewContainer);
        mySeekBar = findViewById(R.id.sb_progress);
        mySeekBar.setMax(SEEK_BAR_MAX);
        mDurationMSec = getDurationMSec();

        myUIHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case MSG_UPDATE_PROGRESS:
                        mySeekBar.setProgress((mySeekBar.getProgress() + 1) % SEEK_BAR_MAX);
                        if (myCurEffectView != null){
                            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams((int)(mySeekBar.getProgress() * mySeekBar.getWidth() / SEEK_BAR_MAX - myCurEffectView.getX()), ViewGroup.LayoutParams.MATCH_PARENT);
                            myCurEffectView.setLayoutParams(lp);
                        }
                        break;
                }

            }
        };
    }

    private void initAndAddEffectView(int color){
        myCurEffectView = new View(myContext);
        myCurEffectView.setBackgroundColor(color);
        myCurEffectView.setX(mySeekBar.getProgress() * mySeekBar.getWidth() / SEEK_BAR_MAX);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        myEffectViews.addView(myCurEffectView, lp);
    }

    private void startTimer(){
        if (myTimerTask != null){
            return;
        }

        mDurationMSec = getDurationMSec();
        if (null == myTimer){
            myTimer = new Timer();
        }

        if (null == myTimerTask){
            myTimerTask = new TimerTask() {
                @Override
                public void run() {
                    myUIHandler.sendEmptyMessage(MSG_UPDATE_PROGRESS);
                }
            };
        }
        if (myTimer != null && myTimerTask != null){
            myTimer.schedule(myTimerTask, 0,  mDurationMSec / SEEK_BAR_MAX);
        }
    }

    private void stopTimer(){
        if (myTimer != null){
            myTimer.cancel();
            myTimer = null;
        }
        if (myTimerTask != null){
            myTimerTask.cancel();
            myTimerTask = null;
        }
        if (myUIHandler != null){
            myUIHandler.removeCallbacks(null);
        }
        myCurEffectView = null;
    }
}
