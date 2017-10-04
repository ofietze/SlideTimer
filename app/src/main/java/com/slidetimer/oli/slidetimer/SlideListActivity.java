package com.slidetimer.oli.slidetimer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SlideListActivity extends AppCompatActivity {

    private int duration;
    private int numOfSlides;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_list);

        SharedPreferences pref = getSharedPreferences("Presentation", 0);
        duration = pref.getInt("duration", 0);
        numOfSlides = pref.getInt("slides",0);

    }

}
