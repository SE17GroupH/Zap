package teamh.zapapp;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    //layout variables
    Button bt_Register;
    EditText et_email, et_passwd, et_rpasswd;
    String email, passwd, rpasswd, result;
    Spinner spinnerAuthmodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();


    }

    public void init(){
        final Context context = getApplicationContext();

        bt_Register = (Button) findViewById(R.id.register);
        bt_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result = null;
                et_email = (EditText) findViewById(R.id.edittext_register_email);
                et_passwd = (EditText) findViewById(R.id.edittext_register_password);
                et_rpasswd = (EditText) findViewById(R.id.edittext_register_retypepassword);

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
                    HttpURLConnection client = null;
                    String zapserver_url = "https://zapserver.herokuapp.com/api/users";
                    String json_data = String.format("{ \"user\": { \"email\": \"%s\", \"password\": \"%s\", \"password_confirmation\": \"%s\" } }",email, passwd, rpasswd);

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

                        if(responseCode == HttpURLConnection.HTTP_CREATED){
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
            }
        });
        //populate spinner with list of strings
        spinnerAuthmodes = (Spinner) findViewById(R.id.spinner_register_authmode);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.register_authmodes, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerAuthmodes.setAdapter(spinnerAdapter);


    }
}
