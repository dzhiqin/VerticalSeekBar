package com.example.verticalseekbar;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private VerticalSeekBar verticalSeekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verticalSeekBar=(VerticalSeekBar)findViewById(R.id.my_seekBar);
        verticalSeekBar.setGears(5);
        verticalSeekBar.setProgressedColor(Color.BLUE);
        //verticalSeekBar.setBackgroundColor(Color.YELLOW);
        verticalSeekBar.setOnTouchListener(new VerticalSeekBar.OnTouchListener() {
            @Override
            public void onTouchMove(int progress) {
                Log.v("test","progress="+progress);
            }

            @Override
            public void onTouchUp(int progress) {
                Log.v("test","up_progress="+progress);
            }
        });
    }
}
