package com.slidetimer.oli.slidetimer;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Custom View Holder to display to EditText Fields
 */

public class ViewHolder {
    private View row;
    private EditText titleText = null, durationText = null;


    public ViewHolder(View row) {
        this.row = row;
    }

    public EditText getTitleText() {
        if (this.titleText == null) {
            this.titleText = (EditText) row.findViewById(R.id.edit_title);
        }
        return this.titleText;
    }

    public EditText getDurationText() {
        if (this.durationText == null) {
            this.durationText = (EditText) row.findViewById(R.id.edit_slideduration);
        }
        return this.durationText;
    }
}

