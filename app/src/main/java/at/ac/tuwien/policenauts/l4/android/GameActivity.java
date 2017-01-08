package at.ac.tuwien.policenauts.l4.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import at.ac.tuwien.policenauts.l4.R;
import at.ac.tuwien.policenauts.l4.game.Game;

/**
 * The actual game runs on this activity.
 *
 * @author Wassily Bartuska
 */
public class GameActivity extends AppCompatActivity {
    private Game game;
    private BroadcastReceiver pauseReceiver;

    /**
     * Invoked when an instance of the class is created.
     *
     * @param savedInstanceState Bundle object passed to this method.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Initialize Audio Manager
        AudioManager aM =
                (AudioManager)getSystemService(GameActivity.AUDIO_SERVICE);

        //Set volume for AM
        aM.setStreamVolume(AudioManager.STREAM_MUSIC,
                aM.getStreamVolume(AudioManager.STREAM_MUSIC), 0);

        // Set up receiver for pause commands
        pauseReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(pauseReceiver,
                new IntentFilter("game-paused"));

        // Initialize game
        game = ((GameApplication) getApplicationContext()).getGame();
        game.initialize(this, (GameSurfaceView)findViewById(R.id.drawing_area));
    }

    /**
     * Invoked when the Activity is in the background.
     */
    @Override
    protected void onPause() {
        game.pause();
        super.onPause();
    }

    /**
     * Invoked when the Activity is active again after being inactive.
     */
    @Override
    protected void onResume() {
        game.resume();
        super.onResume();
    }

    /**
     * Invoked when the Activity is being removed from memory.
     */
    @Override
    protected void onDestroy() {
        //game.pause();
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(pauseReceiver);
    }

    /**
     * Invoked when the back button is being pressed.
     */
    @Override
    public void onBackPressed() {
        Intent pauseIntent = new Intent(GameActivity.this, PauseMenuActivity.class);
        startActivity(pauseIntent);
    }
}
