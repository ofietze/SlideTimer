package com.slidetimer.oli.slidetimer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class EditSlideDetailActivity extends AppCompatActivity {

    private Bundle dataFromslidemdfDetailActivity;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_slide_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);

        EditText editTitle = (EditText) findViewById(R.id.edit_slideTitle);
        final NumberPicker hourPicker = (NumberPicker) findViewById(R.id.edit_pickHour);
        final NumberPicker minutePicker = (NumberPicker) findViewById(R.id.edit_pickMin);
        final NumberPicker secondPicker = (NumberPicker) findViewById(R.id.edit_pickSec);

        hourPicker.setMaxValue(20);
        hourPicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        minutePicker.setMinValue(0);
        secondPicker.setMaxValue(59);
        secondPicker.setMinValue(0);

        dataFromslidemdfDetailActivity = getIntent().getExtras();
        position = dataFromslidemdfDetailActivity.getInt("pos");

        Slide slide = slidemdfListActivity.slideArray[position];

        editTitle.setHint(slide.getTitle());
        hourPicker.setValue(slide.getHour());
        minutePicker.setValue(slide.getMin());
        secondPicker.setValue(slide.getSec());

        String name = dataFromslidemdfDetailActivity.getString("name");
        getSupportActionBar().setTitle(name + " - Slide " + (position+1));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText editTitle = (EditText) findViewById(R.id.edit_slideTitle);

                View parentLayout = findViewById(android.R.id.content);

                String slideTitle;
                if(editTitle.getText().toString().isEmpty()) {
                    slideTitle = editTitle.getHint().toString();
                } else {
                    slideTitle = editTitle.getText().toString();
                }

                Slide editedSlide = slidemdfListActivity.slideArray[position];
                editedSlide.setTitle(slideTitle);
                editedSlide.setDuration(hourPicker.getValue(), minutePicker.getValue(), secondPicker.getValue());
                finish();
            }
        });
    }

}
