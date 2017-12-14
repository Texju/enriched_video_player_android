package fr.enssat.berrou_couillard.enrichedvideo;

/**
 * Created by berrou on 07/12/17.
 */

public class Chapter {
    private String title;
    private String time;
    private String url;

    public Chapter(String title, String time, String url) {
        this.title = title;
        this.time = time;
        this.setUrl(url);
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
