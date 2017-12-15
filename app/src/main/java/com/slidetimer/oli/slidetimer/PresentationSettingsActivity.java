package com.slidetimer.oli.slidetimer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class PresentationSettingsActivity  extends AppCompatActivity {

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");

        EditText editRedAlarm = (EditText) findViewById(R.id.edit_redAlarmTime);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText editRedAlarm = (EditText) findViewById(R.id.edit_redAlarmTime);

                View parentLayout = findViewById(android.R.id.content);
                if(editRedAlarm.getText().toString().isEmpty()) {
                    Snackbar.make(parentLayout, "Empty Text Field is not allowed.", Snackbar.LENGTH_LONG).show();
                    return;
                }

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", Integer.valueOf(editRedAlarm.getText().toString()));
                setResult(Activity.RESULT_OK,returnIntent);

                finish();
            }
        });
    }

}
