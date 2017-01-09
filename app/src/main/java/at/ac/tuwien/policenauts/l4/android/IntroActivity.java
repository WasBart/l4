package at.ac.tuwien.policenauts.l4.android;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.VideoView;

import java.util.Locale;

import at.ac.tuwien.policenauts.l4.R;

/**
 * Activity responsible for displaying an intro video.
 *
 * @author Wassily Bartuska
 */
public class IntroActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener{

    /**
     * Invoked when an instance of the class is created.
     *
     * @param savedInstanceState Bundle object passed to this method.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set window flags to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Initialize act
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Initialize VideoView:
        VideoView view = (VideoView)findViewById(R.id.videoView);
        view.setOnCompletionListener(this);

        // Get System Language
        String lang = Locale.getDefault().getDisplayLanguage();

        // Set Video URI:
        Uri video;
        if(lang.equals("Deutsch")) {
            video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro_g);
        }
        else {
            video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro);
        }
        view.setVideoURI(video);

        // Start the intro:
        view.start();

        // Skip Button:
        Button skip = (Button) findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    /**
     * Method called when mediaPlayer has finished playing the video.
     *
     * @param mp media Player playing the intro video.
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        finish();
    }
}
