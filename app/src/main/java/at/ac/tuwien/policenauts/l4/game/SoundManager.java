package at.ac.tuwien.policenauts.l4.game;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import at.ac.tuwien.policenauts.l4.R;
import at.ac.tuwien.policenauts.l4.android.GameActivity;

/**
 * Load music for usage in-game.
 *
 * @author Wassily Bartuska
 */
public class SoundManager {

    private final Context context;
    private MediaPlayer mP;

    /**
     * Create and pass the current context to the SoundManager.
     *
     * @param context The application context
     */
    SoundManager(Context context) {
        this.context = context;
    }

    public void setBgm() {
        mP = MediaPlayer.create(context, R.raw.bgm);
        mP.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
            }
        });
        mP.setLooping(true);
    }

    public void startBgm() {
        mP.start();
    }

    public void pauseBgm() { mP.pause(); }

    public void forwardBgm(int ms) { mP.seekTo(ms); };

    public void stopBgm() {
        mP.stop();
    }

    public void releaseBgm() {
        mP.release();
    }

    public int getBgmPos() {
        return mP.getCurrentPosition();
    }
}
