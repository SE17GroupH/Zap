package teamh.zapapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    //layout variables
    Button buttonLogin;
    Button buttonLoginFingerprint;
    Button buttonLoginVoicedna;
    Button buttonLoginMagiclink;

    EditText et_username, et_passwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }


    public void init(){

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
                //TODO Check if the user is registered to this method of auth
                String username = et_username.getText().toString();
                String passwd = et_passwd.getText().toString();

                //example@zapserver.com
                //12345678

                HttpURLConnection client = null;
                String zapserver_url = "https://zapserver.herokuapp.com/api/sessions";
                String json_data = String.format("{ \"session\": { \"email\": \"%s\", \"password\": \"%s\" } }",username, passwd);
                Log.w("ZapApp", json_data);
                String result = null;

                try {
                    client = (HttpURLConnection) ((new URL (zapserver_url).openConnection()));
                    client.setRequestMethod("POST");
                    client.setDoOutput(true);
                    client.setRequestProperty("Content-Type", "application/json");
                    client.setRequestProperty("Accept", "application/vnd.zapserver.v1");
                    client.connect();
                    Log.w("ZapApp", "Connected");

                    //Write
                    OutputStream os = client.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(json_data);
                    writer.close();
                    os.flush();
                    os.close();
                    Log.w("ZapApp", "Data sent");

                    //Read
                    BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream(),"UTF-8"));
                    String line = null;
                    StringBuilder sb = new StringBuilder();

                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    br.close();
                    result = sb.toString();

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(client != null) // Make sure the connection is not null.
                        client.disconnect();
                    Log.w("ZapApp", "disconnected");
                }

                if(result != null) {
                    Log.w("ZapApp", result);
                    startActivity(new Intent(LoginActivity.this, LoginSuccess.class));
                } else {
                    Log.w("ZapApp", "result empty");
                    startActivity(new Intent(LoginActivity.this, LoginFail.class));
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
