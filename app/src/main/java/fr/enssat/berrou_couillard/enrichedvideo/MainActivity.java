package fr.enssat.berrou_couillard.enrichedvideo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.net.Uri;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

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

    }
}
