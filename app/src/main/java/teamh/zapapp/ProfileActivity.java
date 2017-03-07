package teamh.zapapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import okhttp3.OkHttpClient;
import okhttp3.Response;

import static teamh.zapapp.ZapHelper.PREFS_NAME;

/**
 * Created by sid on 2/14/17.
 */


public class ProfileActivity extends AppCompatActivity{
    private Button bt_Logout;
    private Context context;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private String auth_token, url;
    private Response response;
    private final OkHttpClient client = new OkHttpClient();
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        bt_Logout = (Button) findViewById(R.id.btn_logout);
        context = getApplicationContext();
        intent = new Intent(ProfileActivity.this, LoginActivity.class);
        settings = getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
        auth_token = settings.getString("auth_token",null);
        url = String.format(ZapHelper.zaplogout_url, auth_token);

        bt_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("loggedin", false);
                editor.commit();
                //logout api call
                /*if(auth_token!=null){
                    try {
                        response = ZapHelper.delete_zap(client, url);
                        if(response.isSuccessful()){
                            Toast.makeText(context, "LoggedOut!", Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        Log.w("ZapApp","IOException");
                    }
                }
                */
                startActivity(intent);
            }
        });



    }
}


