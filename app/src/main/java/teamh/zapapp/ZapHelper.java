package teamh.zapapp;

import java.io.IOException;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by sid on 2/15/17.
 */



public class ZapHelper {
    private static RequestBody body;
    private static Request request;
    private static Response response;

    public static final String PREFS_NAME = "MyPrefsFile";
    final static String zaplogout_url = "https://zapserver.herokuapp.com/api/sessions/%s";
    final static String zaplogin_url = "https://zapserver.herokuapp.com/api/sessions";
    final static String zapregister_url = "https://zapserver.herokuapp.com/api/users";
    final static String zapmagic_url = "https://zapserver.herokuapp.com/api/magiclinks";
    final static String zapmagicauth_url = "https://zapserver.herokuapp.com/api/check_auth_tokens/";

    public final static MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static Response post_zap(OkHttpClient client, String a_url, String json) throws IOException {
        body = RequestBody.create(JSON, json);
        request = new Request.Builder()
                .url(a_url)
                .addHeader("Accept", "application/vnd.zapserver.v1")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        response = client.newCall(request).execute();
        return response;
    }

    public static Response delete_zap(OkHttpClient client, String a_url) throws IOException {
        request = new Request.Builder()
                .url(a_url)
                .addHeader("Accept", "application/vnd.zapserver.v1")
                .addHeader("Content-Type", "application/json")
                .delete()
                .build();

        response = client.newCall(request).execute();
        return response;
    }
}

class LoginResponse {
    public int id;
    public String email;
    public String created_at;
    public String updated_at;
    public String auth_token;
}

class LoginError {
    public String errors;
}

class EmailError {
    public String error;
}

class RegisterError {
    public Map<String, String[]> errors;
}


