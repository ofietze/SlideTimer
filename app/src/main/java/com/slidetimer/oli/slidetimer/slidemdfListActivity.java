package com.slidetimer.oli.slidetimer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;

/**
 * An activity representing a list of Slides. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link slidemdfDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class slidemdfListActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private Bundle dataFromMain;
    public static Slide[] slideArray;
    private int numOfSlides;
    private String name;
    private String formatTitleAndTime;
    private int durationTotalHour;
    private int durationTotalMin;
    private int durationTotalSec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slidemdf_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        //load previous user inputs
        dataFromMain = getIntent().getExtras();
        durationTotalHour = dataFromMain.getInt("durationHour");
        durationTotalMin = dataFromMain.getInt("durationMin");
        durationTotalSec = dataFromMain.getInt("durationSec");
        numOfSlides = dataFromMain.getInt("numOfSlides");
        name = dataFromMain.getString("name");

        int durationTotalInSec = durationTotalHour * 60 * 60 + durationTotalMin * 60 + durationTotalSec;

        formatTitleAndTime = "%s - %02d:%02d:%02d";

        getSupportActionBar().setTitle( String.format(Locale.ENGLISH, formatTitleAndTime , name, durationTotalHour ,durationTotalMin, durationTotalSec));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        double durationPerSlide = (durationTotalInSec / (double) numOfSlides);

        //create an Array with numerated slides
        slideArray = new Slide[numOfSlides];

        for (int i = 0; i < slideArray.length; i++){
            slideArray[i] = new Slide("Slide " + (i+1), (int)durationPerSlide/3600 , (int)durationPerSlide/60, (int)durationPerSlide % 60 );
        }

        View recyclerView = findViewById(R.id.slidemdf_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.slidemdf_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(slideArray));
    }

    //save entered user input and send it to next activity
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(slidemdfListActivity.this, PresentationActivity.class);

        Bundle b = new Bundle();
        b.putInt("durationHour", durationTotalHour);
        b.putInt("durationMin", durationTotalMin);
        b.putInt("durationSec", durationTotalSec);
        b.putInt("numOfSlides", numOfSlides);
        b.putString("name", name);

        intent.putExtra("bundle", b);
        startActivity(intent);
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private Slide[] SIRV_slides;

        public SimpleItemRecyclerViewAdapter(Slide[] slides) {
            SIRV_slides = slides;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.slidemdf_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            Slide s = SIRV_slides[position];

            holder.aSlide = s;
            holder.sIdView.setText(Integer.toString(position+1));
            holder.sContentView.setText(String.format(Locale.ENGLISH, formatTitleAndTime, SIRV_slides[position].getTitle(), s.getHour(), s.getMin(), s.getSec()));

            holder.sView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putInt(slidemdfDetailFragment.ARG_ITEM_ID, position);
                        slidemdfDetailFragment fragment = new slidemdfDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.slidemdf_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, slidemdfDetailActivity.class);
                        intent.putExtra(slidemdfDetailFragment.ARG_ITEM_ID, position);
                        intent.putExtra("dataBundle", dataFromMain);
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return numOfSlides;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public final View sView;
            public final TextView sIdView;
            public final TextView sContentView;
            public Slide aSlide;

            public ViewHolder(View view) {
                super(view);
                sView = view;
                sIdView = (TextView) view.findViewById(R.id.id);
                sContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + sContentView.getText() + "'";
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //when back button is pressed and user returns to list: update list items and update total time

        setContentView(R.layout.activity_slidemdf_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        //update total time with potentially new values
        durationTotalSec = 0;
        durationTotalMin= 0;
        durationTotalHour = 0;

        for (Slide s: slideArray) {
            durationTotalSec += s.getSec();
            durationTotalMin += s.getMin();
            durationTotalHour += s.getHour();
        }

        durationTotalMin += durationTotalSec/60;
        durationTotalSec = durationTotalSec % 60;

        durationTotalHour += durationTotalMin/60;
        durationTotalMin = durationTotalMin % 60;


        getSupportActionBar().setTitle( String.format(Locale.ENGLISH, formatTitleAndTime , name, durationTotalHour ,durationTotalMin, durationTotalSec));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        View recyclerView = findViewById(R.id.slidemdf_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.slidemdf_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

    }
}
