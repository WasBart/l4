package at.ac.tuwien.policenauts.l4.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import at.ac.tuwien.policenauts.l4.R;

/**
 * Activity responsible for listing the high score.
 */
public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
    }
}
