package teamh.zapapp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void apikey_isCorrect() throws Exception {
        assertEquals(32, BuildConfig.SPEAKER_RECOGNITION_API_KEY_1.length());
    }


    @Test
    public void email_isValid() throws  Exception {
        RegisterActivity reg_activity = new RegisterActivity();
        boolean res1 = Util.emailIsValid("foobarhello.com");
        boolean res2 = Util.emailIsValid("foobar@hello.com");
        assertEquals(true,(!res1 && res2));
    }

    @Test
    public void password_isValid() throws  Exception {

        boolean res1 = Util.passwordIsValid("1234");
        boolean res2 = Util.passwordIsValid("123456");
        assertEquals(true,(!res1 && res2));
    }

}