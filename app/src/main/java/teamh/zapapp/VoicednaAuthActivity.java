package teamh.zapapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.microsoft.cognitive.speakerrecognition.SpeakerVerificationRestClient;
import com.microsoft.cognitive.speakerrecognition.contract.CreateProfileException;
import com.microsoft.cognitive.speakerrecognition.contract.verification.CreateProfileResponse;

import java.io.IOException;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;

public class VoicednaAuthActivity extends AppCompatActivity {

    //permission stuff
    private static final int REQUEST_PERMISSIONS = 200;
    private boolean permissionToRecordAccepted = false;
    private boolean permissionToStoreAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //local variables
    private String filepath1, filepath2, filepath3;
    private Context context;
    private SpeakerVerificationRestClient client;
    private Button record1, record2, record3, register;
    private CreateProfileResponse profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voicedna_auth);

        context = getApplicationContext();
        client = new SpeakerVerificationRestClient(SubKey.sub_key1);
        record1 = (Button) findViewById(R.id.btn_record1);
        record3 = (Button) findViewById(R.id.btn_record2);
        record2 = (Button) findViewById(R.id.btn_record3);
        register = (Button) findViewById(R.id.btn_register);

        //request permissions
        ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);


        record1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String filePath = Environment.getExternalStorageDirectory() + "/audioSample1.wav";
                filepath1 = getFilesDir().getAbsolutePath() + "/audioSample1.wav";
                int color = getResources().getColor(R.color.colorPrimaryDark);
                int requestCode = 0;

                AndroidAudioRecorder.with(VoicednaAuthActivity.this)
                        // Required
                        .setFilePath(filepath1)
                        .setColor(color)
                        .setRequestCode(requestCode)
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


        record2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filepath2 = getFilesDir().getAbsolutePath() + "/audioSample2.wav";
                int color = getResources().getColor(R.color.colorPrimaryDark);
                int requestCode = 0;

                AndroidAudioRecorder.with(VoicednaAuthActivity.this)
                        // Required
                        .setFilePath(filepath2)
                        .setColor(color)
                        .setRequestCode(requestCode)
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


        record3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String filePath = Environment.getExternalStorageDirectory() + "/audioSample3.wav";
                filepath3 = getFilesDir().getAbsolutePath() + "/audioSample3.wav";
                int color = getResources().getColor(R.color.colorPrimaryDark);
                int requestCode = 0;

                AndroidAudioRecorder.with(VoicednaAuthActivity.this)
                        // Required
                        .setFilePath(filepath3)
                        .setColor(color)
                        .setRequestCode(requestCode)
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

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    profile = client.createProfile("en-us");
                    Toast.makeText(context, profile.verificationProfileId.toString(), Toast.LENGTH_LONG).show();
                } catch (IOException | CreateProfileException e) {
                    e.printStackTrace();
                }
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
        if (!(permissionToRecordAccepted && permissionToStoreAccepted) ) finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {

                // Great! User has recorded and saved the audio file
            } else if (resultCode == RESULT_CANCELED) {
                // Oops! User has canceled the recording
            }
        }

    }
}
