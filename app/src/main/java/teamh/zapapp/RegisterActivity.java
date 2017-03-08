package teamh.zapapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
    private String email, passwd, rpasswd,json_request;
    private Response response;
    private final Gson gson = new Gson();
    private RegisterError logine;
    private Intent loginIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);
        init();
    }

    public void init(){
        context = getApplicationContext();
        loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);

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

                if(Util.emailIsValid(email) && Util.passwordIsValid(passwd) && Util.passwordsMatch(passwd, rpasswd)){
                    json_request = String.format("{ \"user\": { \"email\": \"%s\", \"password\": \"%s\", \"password_confirmation\": \"%s\" } }",email, passwd, rpasswd);
                    new LoginRegister().execute(json_request);
                }
                else{
                    Toast.makeText(context, "Invalid Email or Password!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }



    //Background thread to execute Register  API call
    class LoginRegister extends AsyncTask<String, String, Response > {

        protected Response doInBackground(String... strings) {
            String json_request = strings[0];
            OkHttpClient client = new OkHttpClient();
            Response response = null;

            try {
                response = ZapHelper.post_zap(client, ZapHelper.zapregister_url, json_request);

            } catch (IOException e) {
                Log.w("ZapApp","IOException");
            }
            return response;
        }


        protected void onPostExecute(Response response) {

            if (response.isSuccessful()) {
                Toast.makeText(context, "Success!", Toast.LENGTH_LONG).show();
                startActivity(loginIntent);
            } else {
                logine = gson.fromJson(response.body().charStream(), RegisterError.class);
                Toast.makeText(context, String.format("Failed: Email %s", logine.errors.get("email").toString()), Toast.LENGTH_LONG).show();
            }
        }




    }




}
