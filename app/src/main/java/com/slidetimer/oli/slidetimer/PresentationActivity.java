package com.slidetimer.oli.slidetimer;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PresentationActivity extends AppCompatActivity implements View.OnClickListener {

    protected int numOfSlides;
    private double totalDuration;
    private Slide[] slides;
    private Slide currentSlide;
    private int currentPos;
    private TextView titleText;
    private TextView timerText;
    private TextView totalTimerText;
    private ProgressBar slideProg;
    private ProgressBar overallProg;
    private Button startStopBtn;
    private boolean timerSlideRunning;
    private boolean timerTotalRunning;
    private CountDownTimer timerSlide;
    private CountDownTimer timerTotal;

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
        totalDuration = b.getDouble("duration");
        slides = slidemdfListActivity.slideArray;

        startStopBtn = (Button) findViewById(R.id.start_timer_btn);
        Button nextBtn = (Button) findViewById(R.id.next_btn);
        Button prevBtn = (Button) findViewById(R.id.prev_btn);
        startStopBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        prevBtn.setOnClickListener(this);

        titleText = (TextView) findViewById(R.id.title_textView);
        timerText = (TextView) findViewById(R.id.timer_textView);
        totalTimerText = (TextView) findViewById(R.id.timer_total_textView);

        slideProg = (ProgressBar) findViewById(R.id.progressBarSlide);
        overallProg = (ProgressBar) findViewById(R.id.progressBarTotal);

        overallProg.setMax((int)(totalDuration * 60));

        //init view for first slide
        currentPos = 0;
        updateViewForSlide(currentPos);
        timerSlideRunning = false;
        timerTotalRunning = false;
    }

    //updates the textviews,progressbar etc. to show the information of the slide at "position"
    public void updateViewForSlide(int position){
        currentSlide = slides[position];

        titleText.setText(currentSlide.getTitle());
        timerText.setText(currentSlide.getDuration() + " min");
        slideProg.setMax((int) (currentSlide.getDuration() * 60));
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

    public CountDownTimer makeTimerForCurrentSlide(){
        final double duration = currentSlide.getDuration();

        CountDownTimer timer = new CountDownTimer((int) (duration * 60 * 1000), 1000) {

            @TargetApi(Build.VERSION_CODES.N)
            public void onTick(long millisUntilFinished) {
                long secsUntilFinished = millisUntilFinished / 1000;
                timerText.setText(secsUntilFinished +" - Seconds remaining: ");
                slideProg.setProgress((int) (duration * 60 - secsUntilFinished), true);
            }

            public void onFinish() {
                if (currentPos < numOfSlides-1) {
                    updateViewForSlide(++currentPos);
                    timerSlide = makeTimerForCurrentSlide().start();
                }
            }
        }.start();

        return timer;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.next_btn: if (currentPos < numOfSlides -1)updateViewForSlide(++currentPos);break;
            case R.id.prev_btn: if (currentPos > 0) updateViewForSlide(--currentPos);break;
            case R.id.start_timer_btn:
                Slide currentSlide = slides[currentPos];
                final double currentDuration = currentSlide.getDuration();

                if (timerSlideRunning || timerTotalRunning){
                    if (timerSlideRunning) {
                        timerSlide.cancel();
                        timerSlideRunning = false;
                    }

                    if (timerTotalRunning){
                        timerTotal.cancel();
                        timerTotalRunning = false;
                    }
                    startStopBtn.setText("Start");
                }
                else{
                    timerSlide = makeTimerForCurrentSlide();
                    timerSlideRunning = true;

                    //if we start the whole presentation, make custom timer for total duration
                    if (currentPos == 0){
                       timerTotal = new CountDownTimer((int) (totalDuration * 60 * 1000), 1000) {

                            @TargetApi(Build.VERSION_CODES.N)
                            public void onTick(long millisUntilFinished) {
                                long secsUntilFinished = millisUntilFinished / 1000;
                                totalTimerText.setText(secsUntilFinished +" - Seconds total remaining: ");
                                overallProg.setProgress((int) (totalDuration * 60 - secsUntilFinished), true);
                            }

                            public void onFinish() {
                                totalTimerText.setText("Presentation time is over.");
                            }
                        }.start();
                        timerTotalRunning = true;
                    }
                    startStopBtn.setText("Stop");
                }
                break;
        }
    }

}
