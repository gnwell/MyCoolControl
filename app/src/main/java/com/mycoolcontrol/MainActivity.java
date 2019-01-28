package com.mycoolcontrol;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mycoolcontrol.R;
import com.mycoolcontrol.view.MyCoolSeekBar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final int SEEK_BAR_MAX = 500;

    private MyCoolSeekBar mySeekBar;
    private TextView tvCurrentPos;
    private long mDurationMSec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mySeekBar = findViewById(R.id.mySeekBar);
        mDurationMSec = getDurationMSec();
        mySeekBar.setDurationMSec(mDurationMSec);

        tvCurrentPos = findViewById(R.id.txt_currentpos);

        long curMSec = mySeekBar.getCurrentPosMSec();
        tvCurrentPos.setText(String.valueOf(curMSec));
        Button button =  findViewById(R.id.btn_start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDurationMSec = getDurationMSec();
                mySeekBar.setDurationMSec(mDurationMSec);
                mySeekBar.start();
            }
        });

        button =  findViewById(R.id.btn_pause);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySeekBar.stop();
            }
        });


        button =  findViewById(R.id.btn_effect1);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()){
                    mySeekBar.stop();
                    mySeekBar.setEffectColor(Color.BLACK).start();
                }else if (MotionEvent.ACTION_UP == event.getAction()){
                    mySeekBar.stop();
                }
                return false;
            }
        });


        button =  findViewById(R.id.btn_effect2);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()){
                    mySeekBar.stop();
                    mySeekBar.setEffectColor(Color.RED).start();
                }else if (MotionEvent.ACTION_UP == event.getAction()){
                    mySeekBar.stop();
                }
                return false;
            }
        });

        mySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvCurrentPos.setText(String.valueOf(mySeekBar.getCurrentPosMSec()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private long getDurationMSec(){
        long duration = 0;
        EditText edtDurMSec = findViewById(R.id.edt_dur_msec);
        duration = Integer.valueOf(edtDurMSec.getText().toString());
        return duration;
    }
}
