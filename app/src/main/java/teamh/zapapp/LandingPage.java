package teamh.zapapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import static teamh.zapapp.ZapHelper.PREFS_NAME;

/**
 * Created by sid on 2/16/17.
 */

public class LandingPage extends AppCompatActivity{
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean loggedin = settings.getBoolean("loggedin", false);

        if (loggedin) {
            intent = new Intent(LandingPage.this, ProfileActivity.class);
        } else {
            intent = new Intent(LandingPage.this, HompageActivity.class);
        }
        startActivity(intent);
    }
}
