package at.ac.tuwien.policenauts.l4.android;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //fullscreen:
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //initialize VideoView:
        VideoView view = (VideoView)findViewById(R.id.videoView);
        view.setOnCompletionListener(this);

        //Set Video URI:
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro);
        view.setVideoURI(video);

        //Start the intro:
        view.start();
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
