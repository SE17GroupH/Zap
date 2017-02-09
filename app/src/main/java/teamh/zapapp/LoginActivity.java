package teamh.zapapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    //layout variables
    Button buttonLoginFingerprint;
    Button buttonLoginVoicedna;
    Button buttonLoginMagiclink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }


    public void init(){


        buttonLoginFingerprint = (Button) findViewById(R.id.button_login_fingerprint);
        buttonLoginVoicedna = (Button) findViewById(R.id.button_login_voicedna);
        buttonLoginMagiclink = (Button) findViewById(R.id.button_login_magiclink);

        //set onclick listeners for the buttons
        buttonLoginFingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Check if the user is registered to this method of auth

                Intent fingerprintAuthIntent = new Intent(LoginActivity.this, FingerprintAuthActivity.class);
                startActivity(fingerprintAuthIntent);
            }
        });
        buttonLoginVoicedna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Check if the user is registered to this method of auth

                Intent voicednaAuthIntent = new Intent(LoginActivity.this, VoicednaAuthActivity.class);
                startActivity(voicednaAuthIntent);
            }
        });
        buttonLoginMagiclink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Check if the user is registered to this method of auth

                Intent magiclinkAuthIntent = new Intent(LoginActivity.this, MagiclinkAuthActivity.class);
                startActivity(magiclinkAuthIntent);
            }
        });

    }
}
