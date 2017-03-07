package com.twisstosin.andela_alc_challenge;



import android.util.Log;

import com.twisstosin.andela_alc_challenge.Models.GitHubUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by twisstosin on 3/5/2017.
 */
class JSONParser {
    private OkHttpClient client = new OkHttpClient();

    // constructor
    JSONParser() {}

    private String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private JSONObject getJsonObject(String url) throws JSONException {
        // Making HTTP request
        JSONObject Jobject = null;
        try {

            String jsonData = run(url);

            Jobject = new JSONObject(jsonData);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Jobject;
    }

    List<GitHubUser> getUsers() throws UnsupportedEncodingException {
        List<GitHubUser> userList = null;

        String url =  ApiData.API_ROOT_URL+"/search/users?q=+location:lagos+language:java";
        Log.d("JSONPack", "Started trying");
        try {
            JSONObject objecter = getJsonObject(url);
            JSONArray objects = objecter.getJSONArray("items");
            Log.d("JSONPack", "Checked");
            if(objects != null) {
                userList = new ArrayList<>();
                Log.d("JSONPack", "Checked2");
                for (int i = 0; i < objects.length(); i++) {

                    JSONObject track = objects.getJSONObject(i);

                    String userName = track.getString(ApiData.API_USER_NAME).toLowerCase();

                    String serverProfilePicUrl = track.getString(ApiData.API_AVATAR_URL);

                    String serverProfileUrl = track.getString(ApiData.API_PROFILE_URL);


                    GitHubUser user = new GitHubUser(serverProfileUrl,userName,serverProfilePicUrl);

                    userList.add(user);
                }
            }
            Log.d("JSONPack", "Ended Try");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("JSONPack", "Exeption with tried "+ e.getLocalizedMessage());
        }
        return userList;
    }
}