package teamh.zapapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MagiclinkAuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magiclink_auth);
        //test code
        TextView info = (TextView) findViewById(R.id.textview_magiclink_info);

        Intent intent = getIntent();
        String action = intent.getAction();

        Uri data = intent.getData();
        if(data != null){
            info.setText(data.toString());
        }








    }
}
