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


public class RegisterActivity extends AppCompatActivity {

    //layout variables
    Button bt_Register;
    EditText et_email, et_passwd, et_rpasswd;
    //local variables
    private Context context;
    private final OkHttpClient client = new OkHttpClient();
    private String email, passwd, rpasswd, result, json_response, json_request;
    private Response response;
    private final Gson gson = new Gson();
    private LoginResponse login;
    private RegisterError logine;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    public void init(){
        context = getApplicationContext();

        bt_Register = (Button) findViewById(R.id.register);
        et_email = (EditText) findViewById(R.id.edittext_register_email);
        et_passwd = (EditText) findViewById(R.id.edittext_register_password);
        et_rpasswd = (EditText) findViewById(R.id.edittext_register_retypepassword);

        bt_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = et_email.getText().toString();
                passwd = et_passwd.getText().toString();
                rpasswd = et_rpasswd.getText().toString();

                if(email == null || email.trim().equals("")){
                    Toast.makeText(context, "Email field empty!",
                            Toast.LENGTH_LONG).show();
                } else if(passwd == null || passwd.trim().equals("")){
                    Toast.makeText(context, "Invalid password",
                            Toast.LENGTH_LONG).show();
                } else if(rpasswd == null || rpasswd.trim().equals("")){
                    Toast.makeText(context, "Repeat passwords!",
                            Toast.LENGTH_LONG).show();
                } else if(!passwd.equals(rpasswd)){
                    Toast.makeText(context, "Passwords do not match!",
                            Toast.LENGTH_LONG).show();
                } else {
                    json_request = String.format("{ \"user\": { \"email\": \"%s\", \"password\": \"%s\", \"password_confirmation\": \"%s\" } }",email, passwd, rpasswd);
                    try {
                        response = ZapHelper.post_zap(client, ZapHelper.zapregister_url, json_request);
                        if (response.isSuccessful()) {
                            login = gson.fromJson(response.body().charStream(), LoginResponse.class);
                            Toast.makeText(context, "Success!", Toast.LENGTH_LONG).show();

                            Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(loginIntent);
                        } else {
                            logine = gson.fromJson(response.body().charStream(), RegisterError.class);
                            Toast.makeText(context, String.format("Failed: Email %s", logine.errors.get("email")), Toast.LENGTH_LONG).show();
                        }

                    } catch (IOException e) {
                        Log.w("ZapApp","IOException");
                    }
                }
            }
        });
    }
}
