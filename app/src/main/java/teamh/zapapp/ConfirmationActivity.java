package teamh.zapapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Response;

import static teamh.zapapp.ZapHelper.PREFS_NAME;

/**
 * Created by sid on 3/8/17.
 */

public class ConfirmationActivity extends AppCompatActivity {

    //local variables
    private Context context;
    private Gson gson = new Gson();
    private String email, json_request;
    private LoginError logine;
    private SharedPreferences settings;
    private String auth_token;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ConfirmationActivity.this, LoginActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magiclink_login);
        context = getApplicationContext();
        settings = getSharedPreferences(PREFS_NAME, 0);

        auth_token = getAuthToken(getIntent().getData().toString());

        if (!settings.getBoolean("registered", false)){
            Toast.makeText(context, "Login with your password the first time!", Toast.LENGTH_LONG).show();
        }
        else{
            email = settings.getString("email","");
            json_request = String.format("{\"authorization\":{\"email\":\"%s\",\"auth_token\":\"%s\"}}", email, auth_token);
            new ConfirmationActivity.LoginMagic().execute(json_request);
        }
    }

    //parse url for auth token
    private String getAuthToken(String url){
        String[] url_parts = url.split("/");
        String auth_part = url_parts[url_parts.length-1];
        String[] temp = auth_part.split("=");
        return temp[temp.length-1];
    }

    //Background thread to execute Magic-Link Login API call
    class LoginMagic extends AsyncTask<String, String, Response > {

        protected Response doInBackground(String... strings) {
            String json_request = strings[0];
            Response response = null;
            try {
                response = ZapHelper.post_zap(new OkHttpClient(), ZapHelper.zapmagicauth_url, json_request);
            } catch (IOException e) {
                Log.w("ZapApp","IOException");
            }
            return response;
        }


        protected void onPostExecute(Response response) {
            if(response == null){
                Toast.makeText(context, String.format("Error. Please check your connection"), Toast.LENGTH_LONG).show();
                return;
            }
            if (response.isSuccessful()) {
                Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ConfirmationActivity.this, ProfileActivity.class));
            } else {
                logine = gson.fromJson(response.body().charStream(), LoginError.class);
                Toast.makeText(context, String.format("Failed: %s", logine.errors), Toast.LENGTH_LONG).show();
                startActivity(new Intent(ConfirmationActivity.this, LoginActivity.class));
            }
        }
    }
}
