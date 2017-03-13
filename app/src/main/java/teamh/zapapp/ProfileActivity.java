package teamh.zapapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profile);
        bt_Logout = (Button) findViewById(R.id.btn_logout);
        context = getApplicationContext();
        settings = getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
        auth_token = settings.getString("auth_token",null);
        url = String.format(ZapHelper.zaplogout_url, auth_token);

        bt_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //logout api call
                /*if(auth_token!=null){
                    try {
                        Response response = ZapHelper.delete_zap(new OkHttpClient(), url);
                        if(response.isSuccessful()){
                            Toast.makeText(context, "LoggedOut!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Log.w("ZapApp","IOException");
                    }
                }
                */

                logout();
            }
        });

    }

    @Override
    public void onBackPressed() {
        logout();
    }

    public void logout(){
        editor.putBoolean("loggedin", false);
        editor.commit();
        Toast.makeText(context, "LoggedOut!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
    }
}


