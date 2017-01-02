package at.ac.tuwien.policenauts.l4.game;


import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;

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
    private SoundPool soundPool;
    private boolean soundLoad = false;
    private int worldSoundID;

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

    public void initSp(int streams) {

        if(Build.VERSION.SDK_INT < 21) {
            soundPool = new SoundPool(streams, AudioManager.STREAM_MUSIC, 0);
        }
        else {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION) .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(attributes).setMaxStreams(streams) .build();
        }

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {
                soundLoad = true;
            }
        });

    }

    public void loadWorld(Context context) {
        soundLoad = false;
        worldSoundID = soundPool.load(context, R.raw.world, 1);
    }

    public void playWorld() {
        if(Build.VERSION.SDK_INT < 21) {
            soundPool.play(worldSoundID, 1, 1, 1, 0, 1.0f);
        }
        else {
            if (soundLoad) {
                soundPool.play(worldSoundID, 1, 1, 1, 0, 1.0f);
            }
        }
    }

    public void pauseSounds() {
        soundPool.autoPause();
    }

    public void resumeSounds() {
        soundPool.autoResume();
    }

    public void releaseSounds() {
        soundPool.release();
    }
}
