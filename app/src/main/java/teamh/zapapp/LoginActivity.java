package teamh.zapapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import static teamh.zapapp.ZapHelper.PREFS_NAME;

public class LoginActivity extends AppCompatActivity {
    //layout variables
    Button buttonLogin;
    Button buttonLoginFingerprint;
    Button buttonLoginVoicedna;
    Button buttonLoginMagiclink;
    EditText et_email, et_passwd;
    //local variables
    private Context context;
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private String email, passwd, json_request;
    private Response response;
    private LoginResponse login;
    private LoginError logine;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private Intent profileIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    protected void save(){
        editor.putBoolean("loggedin", true);
        editor.putString("auth_token", login.auth_token);
        editor.putString("email", email);
        editor.putString("password", passwd);
        editor.putBoolean("isUserVoiceRegistered", false);
        editor.commit();
    }

    public void init(){
        profileIntent = new Intent(LoginActivity.this, ProfileActivity.class);
        context = getApplicationContext();
        settings = getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
        et_email = (EditText) findViewById(R.id.edittext_login_email);
        et_passwd = (EditText) findViewById(R.id.edittext_password);

        buttonLogin = (Button) findViewById(R.id.button_login);
        buttonLoginFingerprint = (Button) findViewById(R.id.button_login_fingerprint);
        buttonLoginVoicedna = (Button) findViewById(R.id.button_login_voicedna);
        buttonLoginMagiclink = (Button) findViewById(R.id.button_login_magiclink);

        //set onclick listeners for the buttons
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = et_email.getText().toString();
                passwd = et_passwd.getText().toString();
                json_request = String.format("{ \"session\": { \"email\": \"%s\", \"password\": \"%s\" } }",email, passwd);

                if (!email.contains("@")) {
                    Toast.makeText(context, "Invalid Email!", Toast.LENGTH_SHORT).show();
                    return;
                } else if(passwd.length() < 6){
                    Toast.makeText(context, "Password too small!", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    response = ZapHelper.post_zap(client, ZapHelper.zaplogin_url, json_request);
                    if (response.isSuccessful()) {
                        login = gson.fromJson(response.body().charStream(), LoginResponse.class);
                        Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show();
                        save();
                        startActivity(profileIntent);
                    } else {
                        logine = gson.fromJson(response.body().charStream(), LoginError.class);
                        Toast.makeText(context, String.format("Failed: %s", logine.errors), Toast.LENGTH_LONG).show();
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
                //TODO Check if user is registered via voice or not
                boolean isUserVoiceRegistered = settings.getBoolean("isUserVoiceRegistered", false);
                if(!isUserVoiceRegistered){
                    //For instances where the user hasn't yet registered with voice
                    Intent voicednaAuthIntent = new Intent(LoginActivity.this, VoicednaAuthActivity.class);
                    startActivity(voicednaAuthIntent);
                }
                else{
                    //For instances when the user has already registered with voice
                    Intent voiceLoginIntent = new Intent(LoginActivity.this, VoiceLoginActivity.class);
                    startActivity(voiceLoginIntent);
                }

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


