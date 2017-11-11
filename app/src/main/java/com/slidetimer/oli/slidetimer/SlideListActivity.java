package com.slidetimer.oli.slidetimer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.StringDef;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class SlideListActivity extends AppCompatActivity implements View.OnClickListener {

    private SlideListAdapter adapter;
    private Bundle dataFromMain;
    private Slide[] slideArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarList);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabList);
        fab.setOnClickListener(this);
        ListView listView = (ListView) findViewById(R.id.list);

        //load previous user inputs
        dataFromMain = getIntent().getExtras();
        double duration = dataFromMain.getDouble("duration");
        int numOfSlides = dataFromMain.getInt("numOfSlides");
        String name = dataFromMain.getString("name");

        getSupportActionBar().setTitle(name);

        double durationPerSlide =  Math.floor(duration / (double) numOfSlides);

        //create an Array with numerated slides
        slideArray = new Slide[numOfSlides];
        String[] slideStrings = new String[numOfSlides];
        for (int i = 0; i < slideArray.length; i++){
            slideArray[i] = new Slide("Slide " + (i+1), durationPerSlide);
            slideStrings[i] = "Slide " + (i+1);
        }

        //create new SlideListAdapter with newly created Array and average duration per slide
        adapter = new SlideListAdapter(this, R.layout.slide_list_content,  slideStrings, getLayoutInflater(), durationPerSlide, slideArray);

        listView.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        if (view.getId()== R.id.fabList){   //if button is clicked save user input an dpack it into a bundle

            for (Slide s: slideArray){
                Log.d("DEBUGGING",s.getTitle() + " " + s.getDuration());
            }

            Intent intent = new Intent(SlideListActivity.this, PresentationActivity.class);
            intent.putExtra("bundle", dataFromMain);
            intent.putExtra("slides", slideArray);
            startActivity(intent);

        }
    }
}
