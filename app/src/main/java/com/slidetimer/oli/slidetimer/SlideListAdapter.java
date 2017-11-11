package com.slidetimer.oli.slidetimer;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

/**
 * Own implementation of ArrayAdapter to display generic slides and the average duration per slide
 */

public class SlideListAdapter extends ArrayAdapter<String> {

    String[] slideStrings;
    Slide[] slideArray;
    LayoutInflater layoutInflater;
    double averageDurationSlide;


    public SlideListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull String[] data, LayoutInflater layoutInflater, double duration, Slide[] slides) {
        super(context, resource, data);
        slideStrings = data;
        this.layoutInflater = layoutInflater;
        averageDurationSlide = duration;
        slideArray = slides;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
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
            holder.getDurationText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b && !((EditText) view).getText().toString().equals("")) {
                        int slideIndex = view.getId();
                        double enteredTime = Double.valueOf(((EditText) view).getText().toString());
                        slideArray[position].setDuration(enteredTime);
                    }
                }
            });
            holder.getTitleText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b){
                        int slideIndex = view.getId();
                        String enteredTitle = ((EditText) view).getText().toString();
                        slideArray[position].setTitle(enteredTitle);
                    }
                }
            });
        }
        return convertView;
    }
}
