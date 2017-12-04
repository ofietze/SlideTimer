package com.slidetimer.oli.slidetimer;

import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PresentationActivity extends AppCompatActivity implements View.OnClickListener {

    protected int numOfSlides;
    private double duration;
    private Slide[] slides;
    private int currentPos;
    private TextView titleText;
    private TextView timerText;
    private ProgressBar slideProg;
    private ProgressBar overallProg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);
        Bundle dataFromSlideList = getIntent().getExtras();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPresentation);
        setSupportActionBar(toolbar);

        Bundle b = dataFromSlideList.getBundle("bundle");
        getSupportActionBar().setTitle(b.getString("name"));

        numOfSlides = b.getInt("numOfSlides");
        duration = b.getDouble("duration");
        slides = slidemdfListActivity.slideArray;

        Button startStopBtn = (Button) findViewById(R.id.start_timer_btn);
        Button nextBtn = (Button) findViewById(R.id.next_btn);
        Button prevBtn = (Button) findViewById(R.id.prev_btn);
        startStopBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        prevBtn.setOnClickListener(this);

        titleText = (TextView) findViewById(R.id.title_textView);
        timerText = (TextView) findViewById(R.id.timer_textView);

        slideProg = (ProgressBar) findViewById(R.id.progressBarSlide);
        overallProg = (ProgressBar) findViewById(R.id.progressBarTotal);

        overallProg.setMax((int)duration);

        //init view for first slide
        currentPos = 0;
        updateViewForSlide(currentPos);
    }

    //updates the textviews,progressbar etc. to show the information of the slide at "position"
    public void updateViewForSlide(int position){
        Slide currentSlide = slides[position];

        titleText.setText(currentSlide.getTitle());
        timerText.setText(currentSlide.getDuration() + " min");
        slideProg.setMax((int) currentSlide.getDuration());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_presentation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.next_btn: if (currentPos < numOfSlides -1)updateViewForSlide(++currentPos);break;
            case R.id.prev_btn: if (currentPos > 0) updateViewForSlide(--currentPos);break;
            case R.id.start_timer_btn: break;
        }
    }

}
