package fr.enssat.berrou_couillard.enrichedvideo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;
import android.widget.VideoView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Info";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Vidéo view
        VideoView vidView = (VideoView)findViewById(R.id.videoView);
        String vidAddress = "http://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4";
        Uri vidUri = Uri.parse(vidAddress);
        vidView.setVideoURI(vidUri);
        vidView.start();
        // Vidéo Control
        MediaController vidControl = new MediaController(this);
        vidControl.setAnchorView(vidView);
        vidView.setMediaController(vidControl);
        // Webview
        WebView browser = (WebView) findViewById(R.id.webView);
        browser.setWebViewClient(new MyWebViewClient());
        browser.getSettings().setJavaScriptEnabled(true);
        browser.loadUrl("http://www.wikipedia.com");


        try {
            InputStream raw = null;
            raw = new FileInputStream(new File("./movies.xml"));
            Log.v(TAG,"rawwwwwwwwwwww: "+raw.toString());
            XmlParser.parse(raw);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    // custom web view client class who extends WebViewClient
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url); // load the url
            return true;
        }
    }
}

