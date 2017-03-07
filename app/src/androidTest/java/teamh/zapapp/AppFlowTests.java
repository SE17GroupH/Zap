package teamh.zapapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Created by kaustubh on 3/7/17.
 */

@RunWith(AndroidJUnit4.class)
public class AppFlowTests {


    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("teamh.zapapp", appContext.getPackageName());
    }


    @Rule
    public ActivityTestRule<HompageActivity> mHomepageActivityTestRule = new ActivityTestRule<HompageActivity>(HompageActivity.class);

    @Test
    public void basicValidAppFlow() throws Exception {
        onView(withId(R.id.button_homepage_login)).perform(click());
        onView(withId(R.id.edittext_login_email)).check(matches(isDisplayed()));
        onView(withId(R.id.edittext_password)).check(matches(isDisplayed()));
        onView(withId(R.id.edittext_login_email)).perform(typeText("sid.sharma0@gmail.com"));
        onView(withId(R.id.edittext_password)).perform(typeText("1234567"));
        onView(withId(R.id.button_login)).perform(click());
        onView(withId(R.id.btn_logout)).check(matches(isDisplayed()));
    }




}
