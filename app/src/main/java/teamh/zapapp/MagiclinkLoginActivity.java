package teamh.zapapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Response;

import static teamh.zapapp.ZapHelper.PREFS_NAME;

public class MagiclinkLoginActivity extends AppCompatActivity {

    //local variables
    private Context context;
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private String email, passwd, json_request;
    private Response response;
    private LoginResponse login;
    private LoginError logine;
    private EmailError loginm;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private Intent intent;
    private String auth_token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magiclink_login);
        context = getApplicationContext();
        settings = getSharedPreferences(PREFS_NAME, 0);
        //trigger this activity when the user clicks on the magic link
        intent = getIntent();
        Uri data = intent.getData();

        //get auth token
        String url = data.toString();
        auth_token = getAuthToken(url);

        //Login via auth token
        //Check if user is registered
        if (!settings.getBoolean("registered", false)){
            //When user hasn't registered for an account
            Toast.makeText(context, "Login with your password the first time!", Toast.LENGTH_LONG).show();

        }
        else{
            email = settings.getString("email","");
            json_request = String.format("{'authorization':{'email':'%s','auth_token':'%s'}}", email, auth_token);
            System.out.println(json_request);
            Toast.makeText(context, json_request, Toast.LENGTH_LONG).show();
            try {
                response = ZapHelper.post_zap(client, ZapHelper.zapmagic_url, json_request);
                if (response.isSuccessful()) {
                    login = gson.fromJson(response.body().charStream(), LoginResponse.class);
                    Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show();
                    //start login activity
                    Intent profileIntent = new Intent(MagiclinkLoginActivity.this, ProfileActivity.class);
                    startActivity(profileIntent);
                } else if (response.code()==401){
                    loginm = gson.fromJson(response.body().charStream(), EmailError.class);
                    Toast.makeText(context, String.format("Failed: %s", loginm.error), Toast.LENGTH_LONG).show();
                } else {
                    logine = gson.fromJson(response.body().charStream(), LoginError.class);
                    Toast.makeText(context, String.format("Failed: %s", logine.errors), Toast.LENGTH_LONG).show();
                }

            } catch (IOException e) {
                Log.w("ZapApp","IOException");
            }




        }



        TextView tv = (TextView) findViewById(R.id.textview_magiclogin_display);
        tv.setText(getAuthToken(url));
    }

    //parse url for auth token
    private String getAuthToken(String url){
        String[] url_parts = url.split("/");
        String auth_part = url_parts[url_parts.length-1];
        String[] temp = auth_part.split("=");
        return temp[temp.length-1];
    }
}