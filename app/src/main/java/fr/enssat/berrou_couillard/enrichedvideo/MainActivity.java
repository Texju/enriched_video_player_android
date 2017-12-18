package fr.enssat.berrou_couillard.enrichedvideo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ExpandableListView;
import android.widget.MediaController;
import android.widget.VideoView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Movie> movies = null;
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

        // Ajout des bouttons pour les chapitres
        // Définition du Layout à construire.
        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id. expandableListView);
        // vider la liste
        // Définition des buttons
        for(Chapter chap : list_chapter) {
            // ajout des éléments au expandableListView
            Log.v(TAG, chap.getTitle());
        }

    }

    private int minutesToMili(int minutes, int secondes){
        int seconds_m = (minutes * 1000);
        int minutes_m = (secondes * 1000*60);
        return minutes_m+seconds_m;
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

