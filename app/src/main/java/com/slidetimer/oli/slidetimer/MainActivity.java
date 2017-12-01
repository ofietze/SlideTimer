package com.slidetimer.oli.slidetimer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText durationInput;
    private EditText slidesInput;
    private EditText presentationName;
    private double duration;
    private int slides;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.welcome);

        durationInput = (EditText) findViewById(R.id.edit_duration);
        slidesInput = (EditText) findViewById(R.id.edit_slides);
        presentationName = (EditText) findViewById(R.id.edit_name);
    }

    @Override
    public void onClick(View v) {

        if (v.getId()== R.id.fab){          //if button is clicked save user input

            duration = Double.parseDouble(durationInput.getText().toString());
            slides = Integer.parseInt(slidesInput.getText().toString());
            name = presentationName.getText().toString();

            Intent intent = new Intent(MainActivity.this, slidemdfListActivity.class);
            intent.putExtra("duration", duration);
            intent.putExtra("numOfSlides", slides);
            intent.putExtra("name", name);
            startActivity(intent);
        }
    }
}
