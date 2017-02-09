package teamh.zapapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class RegisterActivity extends AppCompatActivity {

    //layout variables
    Button buttonGoback;
    Spinner spinnerAuthmodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();


    }

    public void init(){

        buttonGoback = (Button) findViewById(R.id.button_register_goback);
        buttonGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterActivity.this.onBackPressed();
            }
        });

        //populate spinner with list of strings
        spinnerAuthmodes = (Spinner) findViewById(R.id.spinner_register_authmode);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.register_authmodes, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerAuthmodes.setAdapter(spinnerAdapter);


    }
}
