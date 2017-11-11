package com.slidetimer.oli.slidetimer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SlideListActivity extends AppCompatActivity implements View.OnClickListener {

    private int duration;
    private int numOfSlides;
    private SlideListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_list);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabList);
        fab.setOnClickListener(this);
        ListView listView = (ListView) findViewById(R.id.list);

        //load previous user inputs
        Bundle dataFromMain = getIntent().getExtras();
        duration = dataFromMain.getInt("duration");
        numOfSlides = dataFromMain.getInt("slides");
        
        double durationPerSlide =  Math.floor(duration/ (double)numOfSlides);

        //create an Array with numerated slides
        String[] slideStringArray = new String[numOfSlides];
        for (int i = 0; i < slideStringArray.length; i++){
            slideStringArray[i] = "Slide " + (i+1);
        }

        //create new SlideListAdapter with newly created Array and average duration per slide
        adapter = new SlideListAdapter(this, R.layout.slide_list_content,  slideStringArray, getLayoutInflater(), durationPerSlide);

        listView.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        if (view.getId()== R.id.fabList){   //if button is clicked save user input an dpack it into a bundle
            Slide[] slideArray = new Slide[numOfSlides];
            for (int i = 0; i < numOfSlides;i++){

            }
        }
    }
}
