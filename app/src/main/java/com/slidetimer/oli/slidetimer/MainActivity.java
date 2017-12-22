package com.slidetimer.oli.slidetimer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private NumberPicker hourPicker;
    private NumberPicker minutePicker;
    private NumberPicker secondPicker;
    private EditText slidesInput;
    private EditText presentationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.welcome);

        hourPicker = (NumberPicker) findViewById(R.id.pickHour);
        minutePicker = (NumberPicker) findViewById(R.id.pickMin);
        secondPicker = (NumberPicker) findViewById(R.id.pickSec);
        slidesInput = (EditText) findViewById(R.id.edit_slides);
        presentationName = (EditText) findViewById(R.id.edit_name);

        hourPicker.setMaxValue(20);
        hourPicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        minutePicker.setMinValue(0);
        secondPicker.setMaxValue(59);
        secondPicker.setMinValue(0);
    }

    @Override
    public void onClick(View v) {

        if (v.getId()== R.id.fab){          //if button is clicked save user input

            View parentLayout = findViewById(android.R.id.content);
            if(slidesInput.getText().toString().isEmpty() || presentationName.getText().toString().isEmpty()) {
                Snackbar.make(parentLayout, "Empty Text Field is not allowed.", Snackbar.LENGTH_LONG).show();
                return;
            }

            int durationHour = hourPicker.getValue();
            int durationMin = minutePicker.getValue();
            int durationSec = secondPicker.getValue();

            int slides = Integer.parseInt(slidesInput.getText().toString());
            String name = presentationName.getText().toString();

            Intent intent = new Intent(MainActivity.this, slidemdfListActivity.class);
            intent.putExtra("durationHour", durationHour);
            intent.putExtra("durationMin", durationMin);
            intent.putExtra("durationSec", durationSec);
            intent.putExtra("numOfSlides", slides);
            intent.putExtra("name", name);
            startActivity(intent);
        }
    }
}
