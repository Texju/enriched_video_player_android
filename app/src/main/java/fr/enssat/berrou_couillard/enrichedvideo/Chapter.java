package fr.enssat.berrou_couillard.enrichedvideo;

/**
 * Created by berrou on 07/12/17.
 */

public class Chapter {

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String title;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String time;

    public Chapter(String title, String time) {
        this.title = title;
        this.time = time;
    }
}
