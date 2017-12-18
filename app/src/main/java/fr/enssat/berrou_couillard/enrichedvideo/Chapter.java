package fr.enssat.berrou_couillard.enrichedvideo;

/**
 * Cette classe est utilisée pour avoir un objet Chapter
 * Elle permet d'utiliser plus facilement les données stokées et plus précisément les chapitres.
 * Elle est utilisée par la classe Movie
 * Cette classe est plutôt côté "Model" c'est-à-dire proche de la base de données
 * @author  Glenn Berrou
 * @author  Julien Couillard
 * @see Movie
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
