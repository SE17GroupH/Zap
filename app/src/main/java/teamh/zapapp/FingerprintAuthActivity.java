package teamh.zapapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pro100svitlo.fingerprintAuthHelper.FahErrorType;
import com.pro100svitlo.fingerprintAuthHelper.FahListener;
import com.pro100svitlo.fingerprintAuthHelper.FingerprintAuthHelper;

public class FingerprintAuthActivity extends AppCompatActivity {

    private FingerprintAuthHelper mFAH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint_auth);
        mFAH = new FingerprintAuthHelper
                .Builder(this, (FahListener) this) //(Context inscance of Activity, FahListener)
                .build();

        if (mFAH.isHardwareEnable()){
            //do some stuff here
        } else {
            //otherwise do
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        mFAH.startListening();
    }



    @Override
    protected void onStop() {
        super.onStop();
        mFAH.stopListening();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFAH.onDestroy();
    }

    //@Override
    public void onFingerprintStatus(boolean authSuccessful, int errorType, CharSequence errorMess) {
        // authSuccessful - boolean that shows auth status
        // errorType - if auth was failed, you can catch error type
        // errorMess - if auth was failed, errorMess will tell you (and user) the reason

        if (authSuccessful){
            // do some stuff here in case auth was successful
        } else if (mFAH != null){
            // do some stuff here in case auth failed
            switch (errorType){
                case FahErrorType.General.LOCK_SCREEN_DISABLED:
                case FahErrorType.General.NO_FINGERPRINTS:
                    mFAH.showSecuritySettingsDialog();
                    break;
                case FahErrorType.Auth.AUTH_NOT_RECOGNIZED:
                    //do some stuff here
                    break;
                case FahErrorType.Auth.AUTH_TO_MANY_TRIES:
                    //do some stuff here
                    break;
            }
        }
    }

    //@Override
    public void onFingerprintListening(boolean listening, long milliseconds) {
        // listening - status of fingerprint listen process
        // milliseconds - timeout value, will be > 0, if listening = false & errorType = AUTH_TO_MANY_TRIES

        if (listening){
            //add some code here
        } else {
            //add some code here
        }
        if (milliseconds > 0) {
            //if u need, u can show timeout for user
        }
    }
}
