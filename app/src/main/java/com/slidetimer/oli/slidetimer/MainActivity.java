package com.slidetimer.oli.slidetimer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText durationInput;
    private EditText slidesInput;
    public static int duration;
    public static int slides;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        durationInput = (EditText) findViewById(R.id.edit_duration);
        slidesInput = (EditText) findViewById(R.id.edit_slides);
    }

    @Override
    public void onClick(View v) {

        if (v.getId()== R.id.fab){          //if button is clicked

            duration = Integer.parseInt(durationInput.getText().toString());
            slides = Integer.parseInt(slidesInput.getText().toString());
            SharedPreferences pref = getSharedPreferences("Presentation",0);//Save data for later use in shared prefs
            SharedPreferences.Editor editor= pref.edit();
            editor.putInt("duration", duration);
            editor.putInt("slides", slides);
            editor.commit();

            startActivity(new Intent(this, SlideListActivity.class));}
    }
}
