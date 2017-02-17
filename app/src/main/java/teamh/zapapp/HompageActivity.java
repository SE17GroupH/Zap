package teamh.zapapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class HompageActivity extends AppCompatActivity {
    //layout variables
    Button button_register;
    Button button_login;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        init();
    }


    public void init(){

        button_register = (Button) findViewById(R.id.button_homepage_register);
        button_login  = (Button) findViewById(R.id.button_homepage_login);

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(HompageActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(HompageActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });
    }
}
