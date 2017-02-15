package teamh.zapapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.OkHttpClient;

public class LoginActivity extends AppCompatActivity {
    //layout variables
    Button buttonLogin;
    Button buttonLoginFingerprint;
    Button buttonLoginVoicedna;
    Button buttonLoginMagiclink;
    EditText et_username, et_passwd;
    //local variables
    Context context;
    final OkHttpClient client = new OkHttpClient();
    String result, username, passwd, json_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    public void init(){
        context = getApplicationContext();
        et_username = (EditText) findViewById(R.id.edittext_login_email);
        et_passwd = (EditText) findViewById(R.id.edittext_password);

        buttonLogin = (Button) findViewById(R.id.button_login);
        buttonLoginFingerprint = (Button) findViewById(R.id.button_login_fingerprint);
        buttonLoginVoicedna = (Button) findViewById(R.id.button_login_voicedna);
        buttonLoginMagiclink = (Button) findViewById(R.id.button_login_magiclink);

        //set onclick listeners for the buttons
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = et_username.getText().toString();
                passwd = et_passwd.getText().toString();
                json_data = String.format("{ \"session\": { \"email\": \"%s\", \"password\": \"%s\" } }",username, passwd);

                try {
                    result = ZapApiHelper.post_zap(client, ZapApiHelper.zaplogin_url, json_data);
                    Toast.makeText(context, result, Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    Log.w("ZapApp","IOException");
                }
            }
        });

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
