package at.ac.tuwien.policenauts.l4.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import at.ac.tuwien.policenauts.l4.R;

/**
 * Activity for the pause menu (ingame).
 *
 * @author Michael Pucher
 */
public class PauseMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pause_menu);
    }
}
