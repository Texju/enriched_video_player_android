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

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Movie> movies = null;
        Movie currentMovie;
        try {
            InputStream is=getResources().openRawResource(R.raw.movies);
            movies = XmlParser.parse(is);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Vidéo view
        VideoView vidView = (VideoView)findViewById(R.id.videoView);
        // Par défault on met la première vidéo
        Log.v(TAG,movies.get(0).getUrl());
        Uri vidUri = Uri.parse(movies.get(0).getUrl());
        vidView.setVideoURI(vidUri);
        vidView.start();
        // Activer Vidéo Control
        MediaController vidControl = new MediaController(this);
        vidControl.setAnchorView(vidView);
        vidView.setMediaController(vidControl);
        // Webview
        WebView browser = (WebView) findViewById(R.id.webView);
        browser.setWebViewClient(new MyWebViewClient());
        browser.getSettings().setJavaScriptEnabled(true);
        // Par défault on commence le film au début et donc avec la
        // WebView avec l'URL du premier chapitre
        List<Chapter> list_chapter = movies.get(0).getChapitres();
        browser.loadUrl(list_chapter.get(0).getUrl());

        for(Chapter chap : list_chapter) {
            // ajout des éléments au expandableListView
            String[] time_chap;
            int time;
            time= minutesToMili(chap.getTime());
            Log.v(TAG, chap.getTitle());
            Log.v(TAG, String.valueOf(time));
        }

        // Ajout des bouttons pour les chapitres
        // Définition du Layout à construire.
        expListView = (ExpandableListView) findViewById(R.id.expandableListView );

        currentMovie=movies.get(0);
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
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
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
            view.loadUrl(url); // load the url
            return true;
        }
    }
}

