package fr.enssat.berrou_couillard.enrichedvideo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ExpandableListView;
import android.widget.MediaController;
import android.widget.VideoView;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Cette classe est la pincipale de notre application
 * C'est la seule activité de l'application
 * @author  Glenn Berrou
 * @author  Julien Couillard
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Info";
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private List<Movie> movies = null;
    private VideoView vidView;
    private MediaController vidControl;
    private Movie currentMovie;
    private Chapter currentChapter;
    private WebView browser;
    private MyWebViewClient myWebViewClient = new MyWebViewClient();

    /**
     * Méthode onCreate utilisé lors de la création de l'activité.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            InputStream is=getResources().openRawResource(R.raw.movies);
            movies = XmlParser.parse(is);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentMovie=movies.get(0);
        currentChapter = currentMovie.getChapitres().get(0);
        // Vidéo view
        vidView = (VideoView)findViewById(R.id.videoView);
        // Par défault on met la première vidéo
        vidView.setVideoURI(Uri.parse(currentMovie.getUrl()));
        vidView.start();
        // Activer Vidéo Control
        vidControl = new MediaController(this);
        vidControl.setAnchorView(vidView);
        vidView.setMediaController(vidControl);
        // Webview
        browser = (WebView) findViewById(R.id.webView);
        browser.setWebViewClient(myWebViewClient);
        browser.getSettings().setJavaScriptEnabled(true);
        // Par défault on commence le film au début et donc avec la
        // WebView avec l'URL du premier chapitre
        //browser.loadUrl(currentChapter.getUrl());
        myWebViewClient.shouldOverrideUrlLoading(browser,currentChapter.getUrl());
        // Définition du Layout à construire.
        expListView = (ExpandableListView) findViewById(R.id.expandableListView );
        // preparing list data
        prepareListData(movies,currentMovie);
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                if (listDataHeader.get(groupPosition).equals("Movies")){
                    for (Movie m: movies){
                        if (m.getTitle().equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))){
                            if (!m.equals(currentMovie)){
                                currentMovie=m;
                                vidView.setVideoURI(Uri.parse(currentMovie.getUrl()));
                                vidView.start();
                                currentChapter = currentMovie.getChapitres().get(0);
                                //browser.loadUrl(currentChapter.getUrl());
                                myWebViewClient.shouldOverrideUrlLoading(browser,currentChapter.getUrl());
                                prepareListData(movies,currentMovie);
                                listAdapter.setNewItems(listDataHeader,listDataChild);
                                break;
                            }
                        }
                    }
                } else {
                    for (Chapter chapter: currentMovie.getChapitres()) {
                        if (chapter.getTitle().equals(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition))) {
                            currentChapter = chapter;
                            vidView.seekTo(minutesToMili(currentChapter.getTime()));
                            //browser.loadUrl(currentChapter.getUrl());
                            myWebViewClient.shouldOverrideUrlLoading(browser,currentChapter.getUrl());
                        }
                    }
                }
                return false;
            }
        });
    }

    /**
     * Méthode pour convertire une durée de type "mm:ss" en milisecondes
     * @param time
     * @return addition des minutes et secondes en milisecondes
     */
    private int minutesToMili(String time){
        String[] time_split = time.split(":");
        int secondes = Integer.parseInt(time_split[1]) * 1000;
        int minutes = Integer.parseInt(time_split[0]) * 1000*60;
        return minutes+secondes;
    }

    /**
     * Méthode pour préparer les données à envoyer à l'expandableListView
     * @param m
     * @param currentMovie
     */
    private void prepareListData(List<Movie> m, Movie currentMovie) {

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Création de 2 headers : movies et chapters
        listDataHeader.add("Movies");
        listDataHeader.add("Chapters");

        // Création des noeuds enfants pour les films et les chapitres
        List<String> movies = new ArrayList<String>();
        for (Movie mo: m){
            movies.add(mo.getTitle());
        }

        List<String> chapters = new ArrayList<String>();
        for (Movie mo: m){
            if (mo.equals(currentMovie)){
                for (Chapter ch: mo.getChapitres()){
                    chapters.add(ch.getTitle());
                }
            }
        }

        // Association des noeuds enfants aux headers
        listDataChild.put(listDataHeader.get(0), movies);
        listDataChild.put(listDataHeader.get(1), chapters);
    }

    /**
     * Classe customisée pour la webView du layout
     */
    private class MyWebViewClient extends WebViewClient {
        /**
         * Méthode qui évite de naviguer sur la WebView comme on le souhaite.
         * Permet d'éviter de naviguer sur d'autre url excepté celles du film dans le fichier movies.xml
         * @param view
         * @param url
         * @return boolean True ou False si nous devons changer l'url courante de la WebView
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.v(TAG, "---------------");
            for(Chapter ch: currentMovie.getChapitres()) {
                if (url.equals(ch.getUrl())){
                    // On peut changer d'url car elle est dans le fichier
                    view.loadUrl(url); // load the url
                    return true;
                }
            }
            // changement d'URL refusé car elle n'est pas en lien avec le film
            view.loadUrl(currentChapter.getUrl());
            return false;
        }
    }
}

