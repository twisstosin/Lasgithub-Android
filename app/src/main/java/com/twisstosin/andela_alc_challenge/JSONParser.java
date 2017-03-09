package com.twisstosin.andela_alc_challenge;



import android.util.Log;

import com.twisstosin.andela_alc_challenge.Models.ApiData;
import com.twisstosin.andela_alc_challenge.Models.GitHubUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
    private int pageCount = 1;

    public void resetPageCount() {
        this.pageCount = 1;
    }

    public boolean isListEnd() {
        return listEnd;
    }

    private boolean listEnd = false;
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

        String url =  ApiData.API_ROOT_URL+"/search/users?q=+location:lagos+language:java&page="+pageCount;
        Log.d("JSONPack", "Started trying");
        try {
            JSONObject objecter = getJsonObject(url);
            JSONArray objects = objecter.getJSONArray("items");
            Log.d("JSONPack", "Checked");
            if(objects != null) {
                if(objects.length() != 0){
                userList = new ArrayList<>();
                Log.d("JSONPack", "Checked2");
                for (int i = 0; i < objects.length(); i++) {

                    JSONObject jsonUser = objects.getJSONObject(i);

                    String userName = jsonUser.getString(ApiData.API_USER_NAME).toLowerCase();

                    String serverProfilePicUrl = jsonUser.getString(ApiData.API_AVATAR_URL);

                    String serverProfileUrl = jsonUser.getString(ApiData.API_PROFILE_URL);


                    GitHubUser user = new GitHubUser(serverProfileUrl,userName,serverProfilePicUrl);

                    userList.add(user);
                }
                pageCount = pageCount+1;
                }
                else
                    listEnd = true;
            }
            Log.d("JSONPack", "Ended Try");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("JSONPack", "Exeption with tried "+ e.getLocalizedMessage());
        }
        return userList;
    }

    public GitHubUser getProfile(GitHubUser gitHubUser) {
        GitHubUser user = null;

        String url =  ApiData.API_ROOT_URL+"/users/"+gitHubUser.Username;
        Log.d("JSONPack", "Started trying");
        try {
            JSONObject objects = getJsonObject(url);
            Log.d("JSONPack", "Checked");
            if(objects != null) {
                user = gitHubUser;
                Log.d("JSONPack", "Checked2");

                    String fullName = objects.getString(ApiData.API_NAME);

                    String followersCount = objects.getString(ApiData.API_FOLLOWERS);

                    String followingCount = objects.getString(ApiData.API_FOLLOWING);

                    String reposCount = objects.getString(ApiData.API_REPOS);

                    user.fullName = fullName;
                    user.reposCount = reposCount;
                    user.followersCount = followersCount;
                    user.followingCount = followingCount;
            }
            Log.d("JSONPack", "Ended Try");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("JSONPack", "Exeption with tried "+ e.getLocalizedMessage());
        }
        return user;
    }

    public String[] getTopRepo (GitHubUser gitHubUser) {
        String[] repo = null;

        String url =  ApiData.API_ROOT_URL+"/users/"+gitHubUser.Username+"/repos?sort=updated";
        Log.d("JSONPack", "Started trying");
        try {
            JSONObject objects = getJsonObject(url);
            JSONArray jsonArray = objects.names();
            Log.d("JSONPack", "Checked");
            if(jsonArray != null) {
                Log.d("JSONPack", "Checked2");

                String fullName = jsonArray.getJSONObject(0).getString("name");

                String starCount = jsonArray.getJSONObject(0).getString("stargazers_count");

                repo = new String[]{fullName,starCount};
            }
            Log.d("JSONPack", "Ended Try");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("JSONPack", "Exeption with tried "+ e.getLocalizedMessage());
        }
        return repo;
    }
}