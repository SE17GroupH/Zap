package teamh.zapapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.microsoft.cognitive.speakerrecognition.contract.EnrollmentException;
import com.microsoft.cognitive.speakerrecognition.contract.verification.CreateProfileResponse;
import com.microsoft.cognitive.speakerrecognition.contract.verification.Enrollment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;

import static teamh.zapapp.ZapHelper.PREFS_NAME;

public class VoicednaAuthActivity extends AppCompatActivity {

    //permission stuff
    private static final int REQUEST_PERMISSIONS = 200;
    private boolean permissionToRecordAccepted = false;
    private boolean permissionToStoreAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //local variables
    private String audio_filepath, audio_filepath1, audio_filepath2, audio_filepath3;
    private FileInputStream audio_file1, audio_file2, audio_file3;
    private Context context;
    private SpeakerVerificationRestClient client;
    private Button record1, record2, record3, register;
    private CreateProfileResponse profile;
    private Enrollment enroll_obj;
    private int color;
    private boolean recorded[] = {false, false, false};
    private int index;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voicedna_auth);

        context = getApplicationContext();
        settings = getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
        audio_filepath = getFilesDir().getAbsolutePath() + "/audioSample%s.wav";
        audio_filepath1 = String.format(audio_filepath,"1");
        audio_filepath2 = String.format(audio_filepath,"2");
        audio_filepath3 = String.format(audio_filepath,"3");
        color = getResources().getColor(R.color.colorPrimaryDark);

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
                index = 0;
                AndroidAudioRecorder.with(VoicednaAuthActivity.this)
                        // Required
                        .setFilePath(audio_filepath1)
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


        record2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = 1;
                AndroidAudioRecorder.with(VoicednaAuthActivity.this)
                        // Required
                        .setFilePath(audio_filepath2)
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


        record3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = 2;
                AndroidAudioRecorder.with(VoicednaAuthActivity.this)
                        // Required
                        .setFilePath(audio_filepath3)
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

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(recorded[0] && recorded[1] && recorded[2])){
                    Toast.makeText(context, "Need 3 Recordings!", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    profile = client.createProfile("en-us");
                    //editor.putString("voice_profileid", "b8aa7a52-5381-4d7b-bc54-24c6853aa261");
                    Toast.makeText(context, "Profile Created!", Toast.LENGTH_SHORT).show();
                    //implement enrollment calls
                    audio_file1 = new FileInputStream(new File(audio_filepath1));
                    client.enroll(audio_file1,profile.verificationProfileId);
                    audio_file1.close();

                    audio_file2 = new FileInputStream(new File(audio_filepath2));
                    client.enroll(audio_file2,profile.verificationProfileId);
                    audio_file2.close();

                    audio_file3 = new FileInputStream(new File(audio_filepath3));
                    enroll_obj = client.enroll(audio_file3,profile.verificationProfileId);
                    audio_file3.close();

                    Toast.makeText(context, "count:"+enroll_obj.enrollmentsCount+" remaining:"+enroll_obj.remainingEnrollments+" phrase: "+enroll_obj.phrase, Toast.LENGTH_LONG).show();
                    //if (enrollment calls succesffull) {
                        //seet voice registered to true
                        //redirect to voice login activity
                    //}
                    editor.putBoolean("voice_registered",true);
                    editor.putString("voice_profileid", profile.verificationProfileId.toString());
                    editor.commit();
                } catch (IOException | CreateProfileException | EnrollmentException e) {
                    e.printStackTrace();
                }
                intent = new Intent(VoicednaAuthActivity.this, VoiceLoginActivity.class);
                startActivity(intent);
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
                Toast.makeText(context, "Recording Successfull!", Toast.LENGTH_SHORT).show();
                recorded[index] = true;
                // Great! User has recorded and saved the audio file
            } else if (resultCode == RESULT_CANCELED) {
                // Oops! User has canceled the recording
                Toast.makeText(context, "Recording Cancelled!", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
