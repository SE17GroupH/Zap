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

import org.apache.commons.io.IOUtils;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    //layout variables
    Button buttonLogin;
    Button buttonLoginFingerprint;
    Button buttonLoginVoicedna;
    Button buttonLoginMagiclink;

    EditText et_username, et_passwd;
    String result, username, passwd, json_data, zapserver_url;
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
            final Context context = getApplicationContext();
            @Override
            public void onClick(View view) {
                //TODO Check if the user is registered to this method of auth
                username = et_username.getText().toString();
                passwd = et_passwd.getText().toString();
                result = null;
                //example@zapserver.com
                //12345678
                HttpURLConnection client = null;
                zapserver_url = "https://zapserver.herokuapp.com/api/sessions";
                json_data = String.format("{ \"session\": { \"email\": \"%s\", \"password\": \"%s\" } }",username, passwd);
                Log.w("ZapApp", json_data);


                try {
                    //SetAttributes
                    client = (HttpURLConnection) ((new URL(zapserver_url).openConnection()));
                    client.setRequestMethod("POST");
                    client.setDoOutput(true);
                    client.setRequestProperty("Content-Type", "application/json");
                    client.setRequestProperty("Accept", "application/vnd.zapserver.v1");

                    //Write
                    DataOutputStream wr = new DataOutputStream(client.getOutputStream());
                    wr.writeBytes(json_data);
                    wr.flush();
                    wr.close();

                    //Connect
                    client.connect();

                    //Read
                    int responseCode = client.getResponseCode();

                    if(responseCode == HttpURLConnection.HTTP_OK){
                        result = IOUtils.toString(client.getInputStream());
                    } else {
                        result = String.format("HTTP error: %s",responseCode);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(client != null) // Make sure the connection is not null.
                        client.disconnect();
                }

                Toast.makeText(context, result,
                        Toast.LENGTH_LONG).show();

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
