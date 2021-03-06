package com.slidetimer.oli.slidetimer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;

import static android.R.color.white;

public class PresentationActivity extends AppCompatActivity implements View.OnClickListener {

    protected int numOfSlides;
    private int totalDurationInSec;
    private int totalDurationHour;
    private int totalDurationMin;
    private int totalDurationSec;
    private Slide[] slides;
    private Slide currentSlide;
    private int currentPos;
    private TextView timerText;
    private TextView totalTimerText;
    private ProgressBar slideProg;
    private ProgressBar overallProg;
    private ImageButton startStopBtn;
    private boolean timerSlideRunning;
    private boolean timerTotalRunning;
    private boolean paused;
    private CountDownTimer timerSlide;
    private CountDownTimer timerTotal;
    private long secsUntilFinishedSlides;
    private long secsUntilFinishedTotal;
    private int alarmBoundary;
    private boolean notified;
    private NotificationManager mNotificationManager;
    private String presentationTitle;
    private String formatTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);
        Bundle dataFromSlideList = getIntent().getExtras();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPresentation);
        setSupportActionBar(toolbar);

        Bundle b = dataFromSlideList.getBundle("bundle");
        presentationTitle = b.getString("name");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        numOfSlides = b.getInt("numOfSlides");
        totalDurationHour = b.getInt("durationHour");
        totalDurationMin = b.getInt("durationMin");
        totalDurationSec = b.getInt("durationSec");

        formatTime = "%02d:%02d:%02d";

        totalDurationInSec = totalDurationHour * 3600 + totalDurationMin *60 +totalDurationSec;

        slides = slidemdfListActivity.slideArray;

        startStopBtn = (ImageButton) findViewById(R.id.start_timer_btn);
        ImageButton nextBtn = (ImageButton) findViewById(R.id.next_btn);
        ImageButton prevBtn = (ImageButton) findViewById(R.id.prev_btn);
        ImageButton stopBtn = (ImageButton) findViewById(R.id.stop_timer_btn);
        startStopBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        prevBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);

        timerText = (TextView) findViewById(R.id.timer_textView);
        totalTimerText = (TextView) findViewById(R.id.timer_total_textView);

        slideProg = (ProgressBar) findViewById(R.id.progressBarSlide);
        overallProg = (ProgressBar) findViewById(R.id.progressBarTotal);

        overallProg.setMax(totalDurationInSec);

        //init view for first slide
        currentPos = 0;
        updateViewForSlide(currentPos);
        timerSlideRunning = false;
        timerTotalRunning = false;
        paused = false;
        alarmBoundary = 25;

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notified = false;
    }

    //updates the textviews,progressbar etc. to show the information of the slide at "position"
    public void updateViewForSlide(int position){

        currentSlide = slides[position];

        getSupportActionBar().setTitle(currentSlide.getTitle() + " - " + presentationTitle);

        DecimalFormat df = new DecimalFormat("##");
        df.setRoundingMode(RoundingMode.CEILING);

        //complicated way to load time from a double value
        timerText.setText(String.format(Locale.ENGLISH, formatTime, currentSlide.getHour(), currentSlide.getMin(), currentSlide.getSec()));
        slideProg.setMax(currentSlide.getDurationSec());
        if (!(timerSlideRunning && timerTotalRunning)) totalTimerText.setText(String.format(Locale.ENGLISH, formatTime, totalDurationHour, totalDurationMin, totalDurationSec));

        notified = false;
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
            Intent i = new Intent(this, PresentationSettingsActivity.class);
            startActivityForResult(i, 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                alarmBoundary = data.getIntExtra("result",0);
            }

        }
    }

    public CountDownTimer makeTimerForCurrentSlide(int dur){
        final int duration = dur;

         timerSlide = new CountDownTimer((int) (duration * 1000), 1000) {

            @TargetApi(Build.VERSION_CODES.N)
            public void onTick(long millisUntilFinished) {
                secsUntilFinishedSlides = millisUntilFinished / 1000;
                timerText.setText(String.format(Locale.ENGLISH, formatTime ,secsUntilFinishedSlides/3600,
                        (secsUntilFinishedSlides/60 %60), secsUntilFinishedSlides % 60));
                slideProg.setProgress((int) (duration - secsUntilFinishedSlides), true);

                //send notification if time for current slide is running out
                if (secsUntilFinishedSlides < alarmBoundary && !notified){
                    makeNotificationForCurrentSlide();
                }

            }

            public void onFinish() {
                if (currentPos < numOfSlides-1) {
                    //update view to next slide and instantly start it's timer
                    updateViewForSlide(++currentPos);
                    timerSlide.cancel();
                    timerSlide = makeTimerForCurrentSlide(currentSlide.getDurationSec()).start();
                }
                //if presentation is over because last slide is over
                if (currentPos == numOfSlides -1){
                    timerText.setTextSize(25);
                    timerText.setText("Slide time is over.");
                    mNotificationManager.cancelAll();
                }
            }
        }.start();

        return timerSlide;
    }

    //send Notification if user is running low on time
    public void makeNotificationForCurrentSlide(){
        Slide s = currentSlide;

        // The id of the channel.
        String CHANNEL_ID = "my_channel_01";
        NotificationCompat.Builder mBuilder =
               new NotificationCompat.Builder(this)
                       .setSmallIcon(R.mipmap.ic_launcher)
                       .setContentTitle(currentSlide.getTitle())
                       .setContentText("Less than " + alarmBoundary +" seconds remaining on this slide.")
                       .setPriority(NotificationCompat.PRIORITY_MAX)
                       .setDefaults(Notification.DEFAULT_ALL);
    // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, PresentationActivity.class);

    // The stack builder object will contain an artificial back stack for the
    // started Activity.
    // This ensures that navigating backward from the Activity leads out of
    // your app to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
    // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(PresentationActivity.class);
    // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

    // mNotificationId is a unique integer your app uses to identify the
    // notification. For example, to cancel the notification, you can pass its ID
    // number to NotificationManager.cancel().
        int mNotificationId = currentPos;

        mNotificationManager.notify(mNotificationId, mBuilder.build());
        notified = true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        timerSlide.cancel();
        timerTotal.cancel();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.next_btn:
                //if we are skipping pages while presenting update and cancel timers accordingly
                if (currentPos < numOfSlides -1){
                    updateViewForSlide(++currentPos);
                    if (timerSlideRunning) {
                        timerSlide.cancel();
                        timerSlide = makeTimerForCurrentSlide(currentSlide.getDurationSec()).start();
                    }
                }
                break;
            case R.id.prev_btn: if (currentPos > 0) updateViewForSlide(--currentPos);break;
            case R.id.start_timer_btn:
                Slide currentSlide = slides[currentPos];
                final double currentDuration = currentSlide.getDurationSec();

                //pause timer if it is running
                if (timerSlideRunning || timerTotalRunning){
                    if (timerSlideRunning) {
                        timerSlide.cancel();
                        timerSlideRunning = false;
                    }

                    if (timerTotalRunning){
                        timerTotal.cancel();
                        timerTotalRunning = false;
                    }
                    startStopBtn.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
                    paused = true;
                }
                else {
                    if (paused) {
                        //resume from pause
                        timerSlide = makeTimerForCurrentSlide((int)secsUntilFinishedSlides);

                        timerTotal = new CountDownTimer(secsUntilFinishedTotal * 1000, 1000) {

                            @TargetApi(Build.VERSION_CODES.N)
                            public void onTick(long millisUntilFinished) {
                                secsUntilFinishedTotal = millisUntilFinished / 1000;
                                totalTimerText.setText(String.format(Locale.ENGLISH, formatTime, secsUntilFinishedTotal/3600, (secsUntilFinishedTotal/60 %60), secsUntilFinishedTotal % 60));
                                overallProg.setProgress((int) (totalDurationInSec - secsUntilFinishedTotal), true);

                                if (secsUntilFinishedTotal < alarmBoundary) {
                                    overallProg.setBackgroundColor(getResources().getColor(R.color.colorRedAlarm));
                                    if(!notified)makeNotificationForCurrentSlide();
                                }
                                else overallProg.setBackgroundColor(getResources().getColor(white));
                            }

                            public void onFinish() {
                                totalTimerText.setText("Presentation time is over.");
                            }
                        }.start();
                        paused = false;
                    } else {
                        //first start
                        timerSlide = makeTimerForCurrentSlide((int)currentDuration);

                        //if we start the whole presentation, make custom timer for total duration
                        if (currentPos == 0) {
                            timerTotal = new CountDownTimer((int) (totalDurationInSec * 1000), 1000) {

                                @TargetApi(Build.VERSION_CODES.N)
                                public void onTick(long millisUntilFinished) {
                                    secsUntilFinishedTotal = millisUntilFinished / 1000;
                                    totalTimerText.setText(String.format(Locale.ENGLISH, formatTime, secsUntilFinishedTotal/3600, (secsUntilFinishedTotal/60 % 60), secsUntilFinishedTotal % 60));
                                    overallProg.setProgress((int) (totalDurationInSec - secsUntilFinishedTotal), true);

                                    if (secsUntilFinishedTotal < alarmBoundary) {
                                        overallProg.setBackgroundColor(getResources().getColor(R.color.colorRedAlarm));
                                        if(!notified)makeNotificationForCurrentSlide();
                                    }
                                    else overallProg.setBackgroundColor(getResources().getColor(white));
                                }

                                public void onFinish() {
                                    totalTimerText.setText("Presentation time is over.");
                                }
                            }.start();
                        }
                    }
                    timerSlideRunning = true;
                    timerTotalRunning = true;
                    startStopBtn.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                }
                break;
            case R.id.stop_timer_btn:
                if (timerSlideRunning) timerSlide.cancel();
                if (timerTotalRunning) timerTotal.cancel();

                startStopBtn.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
                slideProg.setProgress(0);
                overallProg.setProgress(0);

                overallProg.setBackgroundColor(getResources().getColor(white));
                paused = false;
                currentPos = 0;
                updateViewForSlide(currentPos);

                mNotificationManager.cancelAll();

                timerTotalRunning = false;
                timerTotalRunning = false;
                break;
        }
    }

}
