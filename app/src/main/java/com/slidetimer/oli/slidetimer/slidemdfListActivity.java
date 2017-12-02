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
    private double duration;

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
        duration = dataFromMain.getDouble("duration");
        numOfSlides = dataFromMain.getInt("numOfSlides");
        name = dataFromMain.getString("name"); //TODO check if needed

        getSupportActionBar().setTitle(name);

        double durationPerSlide =  Math.floor(duration / (double) numOfSlides);

        //create an Array with numerated slides
        slideArray = new Slide[numOfSlides];

        for (int i = 0; i < slideArray.length; i++){
            slideArray[i] = new Slide("Slide " + (i+1), durationPerSlide);
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

    @Override
    protected void onRestart() {
        super.onRestart();
        //when back button is pressed and user returns to list: update list items

        setContentView(R.layout.activity_slidemdf_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        getSupportActionBar().setTitle(name);

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
        intent.putExtra("bundle", dataFromMain);
        intent.putExtra("slides", slideArray);
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
            holder.aSlide = SIRV_slides[position];
            holder.sIdView.setText(Integer.toString(position));
            holder.sContentView.setText(SIRV_slides[position].getTitle() + " (" + SIRV_slides[position].getDuration()+"min)");

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
}
