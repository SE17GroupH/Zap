package teamh.zapapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.microsoft.cognitive.speakerrecognition.SpeakerVerificationRestClient;
import com.microsoft.cognitive.speakerrecognition.contract.verification.Verification;
import com.microsoft.cognitive.speakerrecognition.contract.verification.VerificationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;

import static teamh.zapapp.ZapHelper.PREFS_NAME;

public class VoiceLoginActivity extends AppCompatActivity {

    //permission stuff
    private static final int REQUEST_PERMISSIONS = 200;
    private boolean permissionToRecordAccepted = false;
    private boolean permissionToStoreAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private int color;
    private String filepath;
    private Context context;
    private boolean recorded = false;
    private Button btn_record, btn_verify;
    private SpeakerVerificationRestClient client;
    private Verification verify;
    private FileInputStream audio_file;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_login);
        settings = getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
        context = getApplicationContext();
        //Log.d("zap: key1", BuildConfig.SPEAKER_RECOGNITION_API_KEY_1);
        //Log.d("zap: key2", BuildConfig.SPEAKER_RECOGNITION_API_KEY_1);
        //client = new SpeakerVerificationRestClient(BuildConfig.SPEAKER_RECOGNITION_API_KEY_1);

        filepath = getFilesDir().getAbsolutePath() + "/audioLogin.wav";
        color = getResources().getColor(R.color.colorPrimaryDark);

        ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);

        btn_record = (Button) findViewById(R.id.btn_record);
        btn_verify = (Button) findViewById(R.id.btn_verify);

        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidAudioRecorder.with(VoiceLoginActivity.this)
                        // Required
                        .setFilePath(filepath)
                        .setColor(color)
                        .setRequestCode(0)
                        // Optional
                        .setSource(AudioSource.MIC)
                        .setChannel(AudioChannel.MONO)
                        .setSampleRate(AudioSampleRate.HZ_16000)
                        .setAutoStart(true)
                        .setKeepDisplayOn(true)
                        // Start recording
                        .record();
            }
        });

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!recorded) {
                    Toast.makeText(context, "Need Recording!", Toast.LENGTH_SHORT).show();
                    return;
                }
                new VoiceVerify().execute("");

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_PERMISSIONS:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                permissionToStoreAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!(permissionToRecordAccepted && permissionToStoreAccepted) ){
            //go to the start of the login activity
            Intent loginpageIntent = new Intent(VoiceLoginActivity.this, LoginActivity.class);
            startActivity(loginpageIntent);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(context, "Recording Successfull!", Toast.LENGTH_SHORT).show();
                recorded = true;
                // Great! User has recorded and saved the audio file
            } else if (resultCode == RESULT_CANCELED) {
                // Oops! User has canceled the recording
                Toast.makeText(context, "Recording Cancelled!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class VoiceVerify extends AsyncTask<String, String, Verification > {

        protected Verification doInBackground(String... strings) {

            try {
                audio_file = new FileInputStream(new File(filepath));
                try {
                    verify = client.verify(audio_file, UUID.fromString(settings.getString("voice_profileid","")));
                } catch (VerificationException | IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (audio_file!=null)
                    try {
                        audio_file.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
            return verify;

        }


        protected void onPostExecute(Verification verify) {
            Toast.makeText(context, verify.result.toString() + " " + verify.confidence.toString(), Toast.LENGTH_LONG).show();
            recorded = false;
            if (verify.result.toString() == "ACCEPT") {
                intent = new Intent(VoiceLoginActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        }
    }
}
