package at.ac.tuwien.policenauts.l4.android;

import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import at.ac.tuwien.policenauts.l4.R;

/**
 * The actual game runs on this activity.
 *
 * @author Wassily Bartuska
 */
public class GameActivity extends AppCompatActivity {

    /**
     * Invoked when an instance of the class is created.
     *
     * @param savedInstanceState Bundle object passed to this method.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Initialize Audio Manager
        AudioManager aM =
                (AudioManager)getSystemService(GameActivity.AUDIO_SERVICE);

        //Set volume for AM
        aM.setStreamVolume(AudioManager.STREAM_MUSIC,
                aM.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

        // Initialize game
        ((GameApplication) getApplicationContext()).getGame().initialize();
    }
}
