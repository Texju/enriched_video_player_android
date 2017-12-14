package fr.enssat.berrou_couillard.enrichedvideo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by berrou on 07/12/17.
 */

public class Movie {
    public Movie(List chapitres) {
        this.chapitres = chapitres;
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

    public List chapitres= new ArrayList();
}
