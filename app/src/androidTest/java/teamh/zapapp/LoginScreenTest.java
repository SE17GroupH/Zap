package teamh.zapapp;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;
import static teamh.zapapp.CurrentActivityUtil.getCurrentActivityClass;

/**
 * Created by sid on 3/7/17.
 */

public class LoginScreenTest {
        @Rule
        public ActivityTestRule<LoginActivity> mLoginActivityTestRule =
                new ActivityTestRule<LoginActivity>(LoginActivity.class);


        @Test
        public void clickMagiclinkButton_failsToOpen() throws Exception {
            onView(withId(R.id.button_login_magiclink))
                    .perform(click());
            assertEquals(LoginActivity.class, getCurrentActivityClass());
        }

        @Test
        public void fillCredentialsClickLogin_openProfileActivity() throws Exception {
            onView(withId(R.id.edittext_login_email)).perform(typeText("sid.sharma0@gmail.com"));
            onView(withId(R.id.edittext_password)).perform(typeText("1234567"));
            onView(withId(R.id.button_login)).perform(click());
            onView(withId(R.id.btn_logout)).check(matches(isDisplayed()));

            pressBack();

            onView(withId(R.id.button_login_fingerprint)).perform(click());
            assertEquals(FingerprintAuthActivity.class, getCurrentActivityClass());

            pressBack();

            onView(withId(R.id.button_login_voicedna)).perform(click());
            assertEquals(VoicedRegisterActivity.class, getCurrentActivityClass());
        }



}
