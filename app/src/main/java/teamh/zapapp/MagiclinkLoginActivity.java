package teamh.zapapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MagiclinkLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magiclink_login);

        //trigger this activity when the user clicks on the magic link
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();

        TextView tv = (TextView) findViewById(R.id.textview_magiclogin_display);
        tv.setText(data.toString());
    }
}
