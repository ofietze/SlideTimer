package com.slidetimer.oli.slidetimer;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A fragment representing a single slide_mdf detail screen.
 * This fragment is either contained in a {@link slidemdfListActivity}
 * in two-pane mode (on tablets) or a {@link slidemdfDetailActivity}
 * on handsets.
 */
public class slidemdfDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Slide aSlide;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public slidemdfDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            aSlide = slidemdfListActivity.slideArray[getArguments().getInt(ARG_ITEM_ID)];

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(aSlide.getTitle());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.slidemdf_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (aSlide != null) {
            ((TextView) rootView.findViewById(R.id.slidemdf_detail)).setText("Duration of this slide: " +aSlide.getDuration()+ " min");
        }

        return rootView;
    }
}
