package at.ac.tuwien.policenauts.l4.android;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import at.ac.tuwien.policenauts.l4.R;
import at.ac.tuwien.policenauts.l4.game.Game;

/**
 * The main menu of L4.
 *
 * @author Michael Pucher
 */
public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Create on click listener for start game button
        final Game game = ((GameApplication) getApplicationContext()).getGame();
        Button newGame = (Button) findViewById(R.id.newgame);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, GameActivity.class);
                startActivity(intent);
                game.startGame();
            }
        });

        // Create on click listener for scoreboard button
        Button scoreboard = (Button) findViewById(R.id.scoreboard);
        scoreboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ScoreAccess.class
                Intent intent = new Intent(MenuActivity.this, ScoreActivity.class);
                startActivity(intent);
            }
        });

        // Create on click listener for help button
        Button help = (Button) findViewById(R.id.instructions);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ScoreAccess.class
                Intent intent = new Intent(MenuActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });

        // Create on click listener for intro button
        Button intro = (Button) findViewById(R.id.intro);
        intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, IntroActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = new Intent(MenuActivity.this, IntroActivity.class);
        startActivity(intent);
    }
}
