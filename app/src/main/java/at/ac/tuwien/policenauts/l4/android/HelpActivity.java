package at.ac.tuwien.policenauts.l4.android;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import at.ac.tuwien.policenauts.l4.R;

/**
 * Activity for the help dialog.
 *
 * @author Wassily Bartuska
 */
public class HelpActivity extends AppCompatActivity {

    /**
     * Invoked when the activity is created.
     *
     * @param savedInstanceState state of the instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

}
