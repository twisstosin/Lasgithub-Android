package com.twisstosin.andela_alc_challenge;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    Button shareProfile;

    Typeface typeface,typefaceBold;

    TextView nameText,usernameText,repoText,followingText,followersText,repoText2,followingText2,followersText2,githubUrl,recentRepoText,topReopText,topRepoText1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        setContentView(R.layout.activity_profile);


        AssetManager assetManager = getApplicationContext().getAssets();

        typeface = Typeface.createFromAsset(assetManager,String.format(Locale.US, "fonts/%s", "gothic.ttf"));
        typefaceBold = Typeface.createFromAsset(assetManager,String.format(Locale.US, "fonts/%s", "gothicb.ttf"));

        initializeComponents();
    }

    void initializeComponents()
    {
        shareProfile = (Button)findViewById(R.id.share_btn);

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

        topReopText = (TextView)findViewById(R.id.top_repo_text);
        topRepoText1 = (TextView)findViewById(R.id.top_repo_text2);

        //Setting the typeface
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
        topReopText.setTypeface(typefaceBold);
        topRepoText1.setTypeface(typeface);
    }
}
