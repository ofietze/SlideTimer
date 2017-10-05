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

public class SlideListActivity extends AppCompatActivity {

    private int duration;
    private int numOfSlides;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_list);

        ListView listView = (ListView) findViewById(R.id.list);

        //load previous user inputs
        SharedPreferences pref = getSharedPreferences("Presentation", 0);
        duration = pref.getInt("duration", 0);
        numOfSlides = pref.getInt("slides",0);
        double durationPerSlide =  duration/ (double)numOfSlides;

        //create an Array with numerated slides
        String[] slideStringArray = new String[numOfSlides];
        for (int i = 0; i < slideStringArray.length; i++){
            slideStringArray[i] = "Slide " + (i+1);
        }

        //create new SlideListAdapter with newly created Array and average duration per slide
        SlideListAdapter adapter = new SlideListAdapter(this, R.layout.slide_list_content,  slideStringArray, getLayoutInflater(), durationPerSlide);

        listView.setAdapter(adapter);

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });*/
    }
}
