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
import android.view.WindowManager;
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
    private FileInputStream audio_file;
    private Context context;
    private SpeakerVerificationRestClient client;
    private Button record, enroll, login;
    private CreateProfileResponse profile;
    private Enrollment enroll_obj;
    private int color;
    private int enrolled = 0;
    private boolean recorded = false;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_voicedna_auth);

        context = getApplicationContext();
        settings = getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
        audio_filepath = getFilesDir().getAbsolutePath() + "/audioSample.wav";
        color = getResources().getColor(R.color.colorPrimaryDark);
        client = new SpeakerVerificationRestClient(BuildConfig.SPEAKER_RECOGNITION_API_KEY_1);
        record = (Button) findViewById(R.id.btn_record);
        enroll = (Button) findViewById(R.id.btn_enroll);

        //request permissions
        ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);

        try {
            profile = client.createProfile("en-us");
            Toast.makeText(context, "Profile Created!", Toast.LENGTH_SHORT).show();
        } catch (IOException | CreateProfileException e) {
            Toast.makeText(context, "Error on Profile Creation!", Toast.LENGTH_LONG).show();
        }

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recorded = false;
                AndroidAudioRecorder.with(VoicednaAuthActivity.this)
                        // Required
                        .setFilePath(audio_filepath)
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


        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!recorded) {
                    Toast.makeText(context, "No recording!", Toast.LENGTH_SHORT).show();
                    return;
                }

                new VoiceEnroll().execute("");

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
                recorded=true;
                // Great! User has recorded and saved the audio file
            } else if (resultCode == RESULT_CANCELED) {
                // Oops! User has canceled the recording
                Toast.makeText(context, "Recording Cancelled!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    class VoiceEnroll extends AsyncTask<String, String, Enrollment > {

        protected Enrollment doInBackground(String... strings) {
            try {
                audio_file = new FileInputStream(new File(audio_filepath));
                enroll_obj = client.enroll(audio_file,profile.verificationProfileId);
                audio_file.close();
            } catch (IOException e) {
                return null;
            }  catch (EnrollmentException e) {
                return null;

            } catch (Exception e){
                Toast.makeText(context, "Failed! Please try again", Toast.LENGTH_LONG).show();
                return null;
            }

            return enroll_obj;
        }


        protected void onPostExecute(Enrollment enroll_obj) {

            if(enroll_obj == null){
                Toast.makeText(context, "Failed! Please try again", Toast.LENGTH_LONG).show();
                return;
            }

            if (enrolled+1 == enroll_obj.enrollmentsCount) {
                enrolled+=1;
                Toast.makeText(context, "Success! Remaining:"+enroll_obj.remainingEnrollments, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Failed! Please try again", Toast.LENGTH_LONG).show();
            }

            if (enrolled == 3){
                Toast.makeText(context, "Success: Registered for voice!", Toast.LENGTH_LONG).show();
                editor.putString("voice_profileid",profile.verificationProfileId.toString());
                editor.putBoolean("voice_registered",true);
                editor.commit();
                intent = new Intent(VoicednaAuthActivity.this, VoiceLoginActivity.class);
                startActivity(intent);
            }

            recorded = false;

        }
    }
}
