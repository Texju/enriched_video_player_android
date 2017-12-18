package fr.enssat.berrou_couillard.enrichedvideo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by berrou on 07/12/17.
 */

public class Movie {

    private String title;
    private List chapitres= new ArrayList();
    private String url;

    public Movie(List chapitres, String url, String title) {
        this.chapitres = chapitres; this.url=url; this.title=title;
    }

    public Movie() {
        this.chapitres = chapitres;
    }

    public List <Chapter> getChapitres() {
        return chapitres;
    }
    public void setChapitres(List chapitres) {
        this.chapitres = chapitres;
    }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
