package teamh.zapapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import static teamh.zapapp.ZapHelper.PREFS_NAME;

/**
 * Created by sid on 2/14/17.
 */


public class ProfileActivity extends AppCompatActivity{
    private Button bt_Logout;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bt_Logout = (Button) findViewById(R.id.btn_logout);

        bt_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings = getSharedPreferences(PREFS_NAME, 0);
                editor = settings.edit();
                editor.putBoolean("loggedin", false);

                // Commit the edits!
                editor.commit();
                Intent fingerprintAuthIntent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(fingerprintAuthIntent);
            }
        });

    }
}
