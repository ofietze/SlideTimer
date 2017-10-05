package com.slidetimer.oli.slidetimer;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.Map;

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
        ViewHolder holder = null;
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
