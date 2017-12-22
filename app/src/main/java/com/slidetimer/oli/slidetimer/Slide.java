package com.slidetimer.oli.slidetimer;

import java.io.Serializable;

/**
 * Representing a Slide in a presentation. Has a modifiable title and duration.
 */

public class Slide implements Serializable {

    private String title;
    private int durationHour;
    private int durationMin;
    private int durationSec;

    Slide(String slideTitle, int durHour, int durMin, int durSec) {
        title = slideTitle;
        durationHour = durHour;
        durationMin = durMin;
        durationSec = durSec;
    }

    public int getHour() {
        return durationHour;
    }

    public int getMin(){
        return durationMin;
    }

    public int getSec(){
        return durationSec;
    }

    public String getTitle() {
        return title;
    }

    public void setDuration(int newDurationHour, int newDurationMin, int newDurationSec){
        durationHour = newDurationHour;
        durationMin = newDurationMin;
        durationSec = newDurationSec;
    }

    public int getDurationSec(){
        return durationSec + durationMin * 60 + durationHour * 3600;
    }

    public void setTitle(String newTitle){
        title = newTitle;
    }
}
