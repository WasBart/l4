package at.ac.tuwien.policenauts.l4.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import at.ac.tuwien.policenauts.l4.R;

/**
 * Activity responsible for displaying an intro video.
 *
 * @author Wassily Bartuska
 */
public class IntroActivity extends AppCompatActivity {

    /**
     * Invoked when an instance of the class is created.
     *
     * @param savedInstanceState Bundle object passed to this method.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }
}
