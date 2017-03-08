package teamh.zapapp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class CredentialValidateTest {
    @Test
    public void apikey_isCorrect() throws Exception {
        assertEquals(32, BuildConfig.SPEAKER_RECOGNITION_API_KEY_1.length());
    }

    @Test
    public void validEmail_succeeds() throws  Exception {
        assertEquals(true, Util.emailIsValid("foobar@hello.com"));
    }

    @Test
    public void invalidEmail_fails() throws Exception {
        assertEquals(false, Util.emailIsValid("foobarhello.com"));
    }

    @Test
    public void emptyEmail_fails() throws Exception {
        assertEquals(false, Util.emailIsValid(""));
    }

    @Test
    public void validPassword_succeeds() throws  Exception {
        assertEquals(true, Util.passwordIsValid("123456"));
    }

    @Test
    public void invalidPassword_fails() throws  Exception {
        assertEquals(false, Util.passwordIsValid("1234"));
    }

    @Test
    public void emptyPassword_fails() throws  Exception {
        assertEquals(false, Util.passwordIsValid(""));
    }

}