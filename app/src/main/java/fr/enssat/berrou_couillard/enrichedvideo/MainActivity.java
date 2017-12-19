package fr.enssat.berrou_couillard.enrichedvideo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Cette classe est la pincipale de notre application
 * C'est la seule activité de l'application
 * @author  Glenn Berrou
 * @author  Julien Couillard
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Info";

    /**
     * Méthode onCreate utilisé lors de la création de l'activité.
     * @param savedInstanceState
     */

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


        myWebViewClient.shouldOverrideUrlLoading(browser,currentChapter.getUrl());
        browser.setWebViewClient(myWebViewClient);
        browser.getSettings().setJavaScriptEnabled(true);
        // Par défault on commence le film au début et donc avec la
        // WebView avec l'URL du premier chapitre
        //browser.loadUrl(list_chapter.get(0).getUrl());
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

    // Preparation des données à envoyer à l'expandableListView
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
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO : Faire une white list des URL pour désactiver les clics dans la webView
            //boolean contains = IntStream.of().anyMatch(x -> x == 4);
            Log.v(TAG, "---------------");
            for(Chapter ch: currentMovie.getChapitres()) {
                Log.v(TAG, ch.getUrl());
                if (url.equals(ch.getUrl())){
                    Log.v(TAG, "TRUE");
                    view.loadUrl(url); // load the url
                    return true;
                }
            }
            view.loadUrl(currentChapter.getUrl());
            return false;
        }
    }
}

