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

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    //layout variables
    Button buttonLogin;
    Button buttonLoginFingerprint;
    Button buttonLoginVoicedna;
    Button buttonLoginMagiclink;
    EditText et_username, et_passwd;
    //local variables
    private Context context;
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private String username, passwd, json_request, json_response;
    private Response response;

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
                json_request = String.format("{ \"session\": { \"email\": \"%s\", \"password\": \"%s\" } }",username, passwd);

                try {
                    response = ZapApiHelper.post_zap(client, ZapApiHelper.zaplogin_url, json_request);
                    if (response.isSuccessful()) {
                        LoginResponse login = gson.fromJson(response.body().charStream(), LoginResponse.class);
                        Toast.makeText(context, "Success!", Toast.LENGTH_LONG).show();
                        Intent profileIntent = new Intent(LoginActivity.this, ProfileActivity.class);
                        startActivity(profileIntent);
                    } else {
                        LoginError login = gson.fromJson(response.body().charStream(), LoginError.class);
                        Toast.makeText(context, String.format("Failed: %s", login.errors), Toast.LENGTH_LONG).show();
                    }

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


