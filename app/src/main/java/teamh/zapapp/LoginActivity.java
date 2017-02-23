package teamh.zapapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
    //private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private String email, passwd, json_request;
    //private Response response;
    private LoginResponse login;
    private LoginError logine;
    private EmailError loginm;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private Intent intent;


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
        editor.putBoolean("registered", true);
        editor.commit();
    }

    public void init(){
        intent = new Intent(LoginActivity.this, ProfileActivity.class);
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
                    Toast.makeText(context, "Password too short!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //start background thread here
                new LoginDefault().execute(json_request);

            }
        });

        buttonLoginFingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Check if the user is registered to this method of auth
                if (!settings.getBoolean("registered", false)){
                    //When user hasn't registered for an account
                    Toast.makeText(context, "Login with your password the first time!", Toast.LENGTH_LONG).show();
                }
                else {
                    intent = new Intent(LoginActivity.this, FingerprintAuthActivity.class);
                    startActivity(intent);
                }

            }
        });
        buttonLoginVoicedna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Check if the user is registered to this method of auth
                //TODO Check if user is registered via voice or not
                if (!settings.getBoolean("registered", false)){
                    //When user hasn't registered for an account
                    Toast.makeText(context, "Login with your password the first time!", Toast.LENGTH_LONG).show();
                }
                else if(settings.getBoolean("voice_registered", false)){
                    //when the user hasn't yet registered with voice
                    intent = new Intent(LoginActivity.this, VoiceLoginActivity.class);
                    startActivity(intent);
                }
                else{
                    //when the user has already registered with voice
                    intent = new Intent(LoginActivity.this, VoicednaAuthActivity.class);
                    startActivity(intent);
                }

            }
        });
        buttonLoginMagiclink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Check if the user is registered to this method of auth
                if (!settings.getBoolean("registered", false)){
                    //When user hasn't registered for an account
                    Toast.makeText(context, "Login with your password the first time!", Toast.LENGTH_LONG).show();
                }
                else {
                    email = settings.getString("email","");
                    json_request = String.format("{\"magiclink\":{\"email\":\"%s\"}}",email);


                    //make API call in background thread
                    new LoginMagicDefault().execute(json_request);
                }
            }
        });
    }





    //Background thread to execute Magic-Link Login API call
    class LoginMagicDefault extends AsyncTask<String, String, Response > {

        protected Response doInBackground(String... strings) {
            String json_request = strings[0];

            OkHttpClient client = new OkHttpClient();
            Response response = null;

            try {
                response = ZapHelper.post_zap(client, ZapHelper.zapmagic_url, json_request);

            } catch (IOException e) {
                Log.w("ZapApp","IOException");
            }
            return response;
        }


        protected void onPostExecute(Response response) {

            if (response.isSuccessful()) {
                Toast.makeText(context, "Email sent!", Toast.LENGTH_LONG).show();
            } else {
                logine = gson.fromJson(response.body().charStream(), LoginError.class);
                Toast.makeText(context, String.format("Failed: %s", logine.errors), Toast.LENGTH_LONG).show();
            }
        }
    }




    //Background thread to execute Default Login Login API call
    class LoginDefault extends AsyncTask<String, String, Response > {

        protected Response doInBackground(String... strings) {
            String json_request = strings[0];
            OkHttpClient client = new OkHttpClient();
            Response response = null;

            try {
                response = ZapHelper.post_zap(client, ZapHelper.zaplogin_url, json_request);

            } catch (IOException e) {
                Log.w("ZapApp","IOException");
            }

            return response;
        }


        protected void onPostExecute(Response response) {

            if (response.isSuccessful()) {
                login = gson.fromJson(response.body().charStream(), LoginResponse.class);
                Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show();
                save();
                startActivity(intent);
            } else if (response.code()==401){
                loginm = gson.fromJson(response.body().charStream(), EmailError.class);
                Toast.makeText(context, String.format("Failed: %s", loginm.error), Toast.LENGTH_LONG).show();
            } else {
                logine = gson.fromJson(response.body().charStream(), LoginError.class);
                Toast.makeText(context, String.format("Failed: %s", logine.errors), Toast.LENGTH_LONG).show();
            }
        }
    }




}


