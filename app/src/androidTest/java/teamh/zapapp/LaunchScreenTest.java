package teamh.zapapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static teamh.zapapp.CurrentActivityUtil.getCurrentActivityClass;


/**
 * Created by sid on 3/7/17.
 */
@RunWith(AndroidJUnit4.class)
public class LaunchScreenTest {

    @Rule
    public ActivityTestRule<HompageActivity> mHomepageActivityTestRule =
            new ActivityTestRule<HompageActivity>(HompageActivity.class);

    @Test
    public void clickLoginButton_openLoginActivity() throws Exception {
        onView(withId(R.id.button_homepage_login))
                .perform(click());
        assertEquals(LoginActivity.class, getCurrentActivityClass());
    }

    @Test
    public void clickRegisterButton_openRegisterActivity() throws Exception {
        onView(withId(R.id.button_homepage_register))
                .perform(click());
        assertEquals(RegisterActivity.class, getCurrentActivityClass());
    }

}
