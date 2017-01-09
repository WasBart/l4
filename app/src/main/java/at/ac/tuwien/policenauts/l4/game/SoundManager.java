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
class SoundManager {

    private final Context context;
    private MediaPlayer mP;
    private SoundPool soundPool;
    private boolean soundLoad = false;
    private boolean mute = false;

    /**
     * Create and pass the current context to the SoundManager.
     *
     * @param context The application context
     */
    SoundManager(Context context) {
        this.context = context;
    }

    /**
     * Sets the backgroundmusic to the soundfile bgm located in the raw folder.
     *
     */
    void setBgm() {
        mP = MediaPlayer.create(context, R.raw.bgm);
        mP.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
            }
        });
        mP.setLooping(true);
    }

    /**
     * Starts the backgroundmusic.
     *
     */
    void startBgm() {
        mP.start();
        if (mute)
            mP.setVolume(0, 0);
    }

    /**
     * Pauses the backgroundmusic.
     *
     */
    void pauseBgm() { mP.pause(); }

    /**
     * Forwards the backgroundmusic to a timestamp.
     *
     * @param ms specified timestamp.
     */
    void forwardBgm(int ms) { mP.seekTo(ms); };

    /**
     * Releases the resources of the backgroundmusic.
     *
     */
    void releaseBgm() {
        mP.release();
    }

    /**
     * Gets the curretn timestamp of backgroundmusic playback.
     *
     * @return current timestamp.
     */
    int getBgmPos() {
        return mP.getCurrentPosition();
    }

    /**
     * Init the soundpool.
     *
     * @param streams number of sound streams that can be active.
     */
    void initSp(int streams) {
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

    /**
     * Load a sound into the soundpool.
     *
     * @param context used context.
     * @param name name of the soundfile.
     * @return handle of the loaded sound.
     */
    int loadSound(Context context, String name) {
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

    /**
     * Play a loaded sound.
     *
     * @param id handle of the sound.
     * @param leftCh left audio channel.
     * @param rightCh right audio channel.
     * @param prior priority of the sound.
     * @param loop specifier for looping.
     * @param speed speed of playback.
     */
    void playSound(int id, int leftCh, int rightCh, int prior, int loop, float speed) {
        if (mute)
            return;
        if(Build.VERSION.SDK_INT < 21) {
            soundPool.play(id, leftCh, rightCh, prior, loop, speed);
        }
        else {
            if (soundLoad) {
                soundPool.play(id, leftCh, rightCh, prior, loop, speed);
            }
        }
    }

    /**
     * Pause all soundpool sound.
     *
     */
    void pauseSounds() {
        soundPool.autoPause();
    }

    /**
     * Resume all soundpool sounds.
     *
     */
    void resumeSounds() {
        soundPool.autoResume();
    }

    /**
     * Release the reseources of the soundpool.
     *
     */
    void releaseSounds() {
        soundPool.release();
    }

    /**
     * Mute all sounds.
     */
    void muteSounds() {
        mute = true;
        mP.setVolume(0, 0);
    }

    /**
     * Unmute all sounds.
     */
    void unmuteSounds() {
        mute = false;
        mP.setVolume(1, 1);
    }
}
