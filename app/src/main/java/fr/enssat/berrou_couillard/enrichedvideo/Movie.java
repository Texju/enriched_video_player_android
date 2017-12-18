package fr.enssat.berrou_couillard.enrichedvideo;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe est utilisée pour avoir un objet Movie
 * Elle permet d'utiliser plus facilement les données stokées.
 * Cette classe est plutôt côté "Model" c'est-à-dire proche de la base de données
 * Elle utilise la classe Chapter
 * @author  Glenn Berrou
 * @author  Julien Couillard
 * @see Chapter
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

    public List getChapitres() {
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
