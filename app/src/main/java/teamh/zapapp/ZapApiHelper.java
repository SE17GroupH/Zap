package teamh.zapapp;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by sid on 2/15/17.
 */

public class ZapApiHelper {
    final static String zaplogin_url = "https://zapserver.herokuapp.com/api/sessions";
    final static String zapregister_url = "https://zapserver.herokuapp.com/api/users";
    public final static MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static String post_zap(OkHttpClient client, String a_url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(a_url)
                .addHeader("Accept", "application/vnd.zapserver.v1")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}
