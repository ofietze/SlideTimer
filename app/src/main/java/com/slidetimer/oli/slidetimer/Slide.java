package com.slidetimer.oli.slidetimer;

/**
 * Representing a Slide in a presentation. Has a modifiable title and duration.
 */

public class Slide {

    String title;
    double duration;

    Slide(String slideTitle, double slideDuration) {
        title = slideTitle;
        duration = slideDuration;
    }

    public double getDuration() {
        return duration;
    }

    public String getTitle() {
        return title;
    }

    public void setDuration(double newDuration){
        duration = newDuration;
    }

    public void setTitle(String newTitle){
        title = newTitle;
    }
}
