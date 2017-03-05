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
public class JSONParser {
    private OkHttpClient client = new OkHttpClient();

    // constructor
    public JSONParser() {}

    private String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public JSONArray getJsonObject (String url) throws JSONException {
        // Making HTTP request
        JSONArray Jobject = null;
        try {

            String jsonData = run(url);

            Jobject = new JSONArray(jsonData);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Jobject;
    }

    public List<GitHubUser> getUsers () throws UnsupportedEncodingException {
        List<GitHubUser> userList = null;

        String url =  ApiData.API_ROOT_URL+"/search/users?q=+location:lagos+language:java";
        try {
            JSONArray objects = getJsonObject(url);
            Log.d("JSONPack", "Checked");
            if(objects != null) {
                userList = new ArrayList<>();
                Log.d("JSONPack", "Checked2");
                for (int i = 0; i < objects.length(); i++) {

                    JSONObject track = objects.getJSONObject(i);

                    String userName = track.getString(ApiData.API_USER_NAME);

                    String serverProfilePicUrl = track.getString(ApiData.API_AVATAR_URL);

                    String serverProfileUrl = track.getString(ApiData.API_PROFILE_URL);


                    GitHubUser user = new GitHubUser(serverProfileUrl,userName,serverProfilePicUrl);

                    userList.add(user);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userList;
    }
}