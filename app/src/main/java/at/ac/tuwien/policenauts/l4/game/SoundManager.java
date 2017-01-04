package at.ac.tuwien.policenauts.l4.game;


import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

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

    public int loadSound(Context context, String name) {
        soundLoad = false;
        int soundID = 0;

        try {
            soundID = soundPool.load(context, R.raw.class.getField(name).getInt(null), 1);
        } catch(NoSuchFieldException nsfe) {
            Log.e("Loading failed: ", "Could not load soundfile with specified name.");
        } catch(IllegalAccessException iae) {
            Log.e("Loading failed: ", "Could not load soundfile with specified name.");
        }
        return soundID;
    }

    public void playSound(int id, int leftCh, int rightCh, int prior, int loop, float speed) {
        if(Build.VERSION.SDK_INT < 21) {
            soundPool.play(id, leftCh, rightCh, prior, loop, speed);
        }
        else {
            if (soundLoad) {
                soundPool.play(id, leftCh, rightCh, prior, loop, speed);
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
