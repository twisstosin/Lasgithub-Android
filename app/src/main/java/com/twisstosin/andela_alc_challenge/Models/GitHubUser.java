package com.twisstosin.andela_alc_challenge.Models;

import com.twisstosin.andela_alc_challenge.R;

/**
 * Created by twisstosin on 3/5/2017.
 */

public class GitHubUser {
    public String profileImageUrl;
    public String Username;
    public String profileUrl;
    public String fullName;
    public String followersCount;
    public String followingCount;
    public String reposCount;

    public GitHubUser(String profileUrl, String username, String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
        this.Username = username;
        this.profileUrl = profileUrl;
    }
}
