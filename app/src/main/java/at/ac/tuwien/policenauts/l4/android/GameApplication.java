package at.ac.tuwien.policenauts.l4.android;

import android.app.Application;

import at.ac.tuwien.policenauts.l4.game.Game;

/**
 * Subclassing application for managing global state, i.e. the Game object.
 */
public class GameApplication extends Application {
    private Game game = null;

    @Override
    public void onCreate() {
        super.onCreate();

        // Create new game instance
        game = new Game(this);
    }

    /**
     * Retrieve the game instance.
     *
     * @return The Game Instance
     */
    public Game getGame() {
        return game;
    }
}
