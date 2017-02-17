package teamh.zapapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.microsoft.cognitive.speakerrecognition.SpeakerVerificationRestClient;
import com.microsoft.cognitive.speakerrecognition.contract.CreateProfileException;
import com.microsoft.cognitive.speakerrecognition.contract.verification.CreateProfileResponse;

import java.io.IOException;

public class VoicednaAuthActivity extends AppCompatActivity {

    private Context context;
    private SpeakerVerificationRestClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voicedna_auth);

        context = getApplicationContext();

        client = new SpeakerVerificationRestClient(ZapHelper.sub_key1);
        try {
            CreateProfileResponse profile = client.createProfile("en-us");
            Toast.makeText(context, profile.toString(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CreateProfileException e) {
            e.printStackTrace();
        }

    }
}
