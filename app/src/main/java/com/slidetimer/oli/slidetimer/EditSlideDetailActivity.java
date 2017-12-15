package com.slidetimer.oli.slidetimer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

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
        EditText editDuration = (EditText) findViewById(R.id.edit_slideDuration);

        dataFromslidemdfDetailActivity = getIntent().getExtras();
        position = dataFromslidemdfDetailActivity.getInt("pos");

        Slide slide = slidemdfListActivity.slideArray[position];

        editTitle.setHint(slide.getTitle());
        editDuration.setHint(df.format(slide.getDuration()) + " min");

        String name = dataFromslidemdfDetailActivity.getString("name");
        getSupportActionBar().setTitle(name + " - Slide " + (position+1));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText editTitle = (EditText) findViewById(R.id.edit_slideTitle);
                EditText editDuration = (EditText) findViewById(R.id.edit_slideDuration);

                View parentLayout = findViewById(android.R.id.content);
                if(editTitle.getText().toString().isEmpty() || editDuration.getText().toString().isEmpty()) {
                    Snackbar.make(parentLayout, "Empty Text Field is not allowed.", Snackbar.LENGTH_LONG).show();
                    return;
                }

                Slide editedSlide = slidemdfListActivity.slideArray[position];
                editedSlide.setTitle(editTitle.getText().toString());
                editedSlide.setDuration(Double.valueOf(editDuration.getText().toString()));
                finish();
            }
        });
    }

}
