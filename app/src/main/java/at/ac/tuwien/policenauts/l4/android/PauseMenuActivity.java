package at.ac.tuwien.policenauts.l4.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

        // Create on click listener for start game button
        Button continueGame = (Button) findViewById(R.id.pause_continue);
        continueGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Create on click listener for start game button
        Button quit = (Button) findViewById(R.id.pause_quit);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
