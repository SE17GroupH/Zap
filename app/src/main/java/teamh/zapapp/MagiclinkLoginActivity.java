package teamh.zapapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Response;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magiclink_login);

        //trigger this activity when the user clicks on the magic link
        intent = getIntent();
        Uri data = intent.getData();

        //get auth token
        String url = data.toString();
        String auth_token = getAuthToken(url);

        //Login via auth token

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
