package com.twisstosin.andela_alc_challenge;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.twisstosin.andela_alc_challenge.Models.ApiData;
import com.twisstosin.andela_alc_challenge.Models.GitHubUser;

import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    Button shareProfile;

    Typeface typeface,typefaceBold;

    GitHubUser user;

    ImageView backgroundImage;
    CircularImageView profileImage;
    RelativeLayout repoLayout;

    JSONParser jsonParser;

    TextView nameText,usernameText,repoText,followingText,followersText,repoText2,followingText2,followersText2,githubUrl,recentRepoText, topRepoText,topRepoText1,refreshText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        jsonParser = new JSONParser();

        setContentView(R.layout.activity_profile);


        AssetManager assetManager = getApplicationContext().getAssets();

        typeface = Typeface.createFromAsset(assetManager,String.format(Locale.US, "fonts/%s", "gothic.ttf"));
        typefaceBold = Typeface.createFromAsset(assetManager,String.format(Locale.US, "fonts/%s", "gothicb.ttf"));

        initializeComponents();

        Intent intent = getIntent();

        Gson gson = new Gson();

        user = gson.fromJson(intent.getStringExtra(ApiData.INTENT_USER),GitHubUser.class);

        setUserValues();
    }

    void initializeComponents()
    {
        shareProfile = (Button)findViewById(R.id.share_btn);
        repoLayout = (RelativeLayout)findViewById(R.id.repo_layout);

        nameText = (TextView)findViewById(R.id.name);
        usernameText = (TextView)findViewById(R.id.username);

        followersText = (TextView)findViewById(R.id.followers_num);
        followersText2 = (TextView)findViewById(R.id.followers_text);
        followingText = (TextView)findViewById(R.id.following_num);
        followingText2 = (TextView)findViewById(R.id.following_text);
        repoText = (TextView)findViewById(R.id.repo_num);
        repoText2 = (TextView)findViewById(R.id.repo_text);

        githubUrl = (TextView)findViewById(R.id.github_url);
        recentRepoText = (TextView)findViewById(R.id.repo_update_text);

        topRepoText = (TextView)findViewById(R.id.top_repo_text);
        topRepoText1 = (TextView)findViewById(R.id.top_repo_text2);

        refreshText = (TextView)findViewById(R.id.tap_get_btn);

        backgroundImage = (ImageView)findViewById(R.id.cover_image);
        profileImage = (CircularImageView)findViewById(R.id.profile_image);

        //Setting the typefaces
        shareProfile.setTypeface(typeface);

        nameText.setTypeface(typefaceBold);
        usernameText.setTypeface(typeface);

        followingText.setTypeface(typeface);
        followingText2.setTypeface(typeface);
        followersText.setTypeface(typeface);
        followersText2.setTypeface(typeface);
        repoText.setTypeface(typeface);
        repoText2.setTypeface(typeface);

        githubUrl.setTypeface(typeface);
        recentRepoText.setTypeface(typeface);
        topRepoText.setTypeface(typefaceBold);
        topRepoText1.setTypeface(typeface);

        refreshText.setTypeface(typeface);
    }

    void setUserValues()
    {
        Glide.with(this)
                .load(user.profileImageUrl)
                .placeholder(R.color.primary)
                .into(backgroundImage);
        Glide.with(this)
                .load(user.profileImageUrl)
                .asBitmap()
                .placeholder(R.drawable.ic_person_black_24dp)
                .into(profileImage);

        nameText.setText(user.fullName);
        usernameText.setText(user.Username);
        followersText.setText(user.followersCount);
        followingText.setText(user.followingCount);
        repoText.setText(user.reposCount);
        githubUrl.setText(user.profileUrl.replace("https://",""));

        //try to get last updated repo details
        refreshRepoFromApi();

        shareProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String formattedString = "Check out this awesome developer "+"@"+user.Username+", "+user.profileUrl+".";
                sendIntent.putExtra(Intent.EXTRA_TEXT, formattedString);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share Profile With"));
            }
        });

        refreshText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshRepoFromApi();
            }
        });
    }

    private void refreshRepoFromApi()
    {
        refreshText.setVisibility(View.GONE);
        Log.d("JSONPack","Started Refresh");

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                String[] gitHubRepo = null;
                try {
                    gitHubRepo = jsonParser.getTopRepo(user);
                    Log.d("JSONPack","Tried to get users");
                } catch (Exception e1) {
                    e1.printStackTrace();
                    Log.d("JSONPack","Exeption");
                }

                final String[] finalGitHubRepo = gitHubRepo;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(finalGitHubRepo != null) {
                            if(finalGitHubRepo.length !=0)
                            {
                                topRepoText.setText(finalGitHubRepo[0]);
                                topRepoText1.setText(finalGitHubRepo[1]);
                                recentRepoText.setVisibility(View.VISIBLE);
                                repoLayout.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                Toast.makeText(ProfileActivity.this, "No Repo to Display", Toast.LENGTH_SHORT).show();
                                //refreshLayout.setVisibility(View.GONE);
                            }
                        }
                        else {
                                Toast.makeText(ProfileActivity.this, "Returned Null", Toast.LENGTH_SHORT).show();
                                refreshText.setVisibility(View.VISIBLE);
                        }
                    }
                });

                return null;
            }
        };
        App.runAsyncTask(task,"");
    }
}
