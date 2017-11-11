package com.slidetimer.oli.slidetimer;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Own implementation of ArrayAdapter to display generic slides and the average duration per slide
 */

public class SlideListAdapter extends ArrayAdapter<String> {

    String[] slideStrings;
    LayoutInflater layoutInflater;
    double averageDurationSlide;


    public SlideListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull String[] data, LayoutInflater layoutInflater, double duration) {
        super(context, resource, data);
        slideStrings = data;
        this.layoutInflater = layoutInflater;
        averageDurationSlide = duration;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = layoutInflater;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.slide_list_content, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        //limit list to number of slides
        if (position < slideStrings.length) {
            holder.getTitleText().setHint(slideStrings[position]);
            holder.getDurationText().setHint(averageDurationSlide + " min");
        }
        return convertView;
    }
}
