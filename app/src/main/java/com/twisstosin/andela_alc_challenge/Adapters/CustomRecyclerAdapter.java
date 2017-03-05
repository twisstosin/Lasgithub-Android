package com.twisstosin.andela_alc_challenge.Adapters;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.twisstosin.andela_alc_challenge.Models.GitHubUser;

import java.util.List;
import java.util.Locale;

import com.twisstosin.andela_alc_challenge.R;

/**
 * Created by twisstosin on 11/27/2016.
 */

public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder> {

    private List<GitHubUser> userList;
    private Context context;

    // Allows to remember the last item shown on screen

    private int lastPosition = 0;

    private MyViewHolder previousView;

    public CustomRecyclerAdapter() {
    }

    public CustomRecyclerAdapter(Context context, List<GitHubUser> userList) {
        this.context = context;
        this.userList = userList;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_layout,parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // Here we apply the animation when the view is bound
        //setAnimation(holder.container, position);

            GitHubUser gitHubUser = userList.get(position);

            holder.artisteTextView.setText(gitHubUser.Username);

        //TODO : Change TypeFace
//            AssetManager assetManager = context.getApplicationContext().getAssets();
//            Typeface typeface = Typeface.createFromAsset(assetManager, String.format(Locale.US, "fonts/%s", "proximanova_light.ttf"));
//            holder.artisteTextView.setTypeface(typeface);

            Glide.with(context)
                    .load(gitHubUser.profileImageUrl)
                    .placeholder(R.drawable.davido)
                    .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
            return userList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView artisteTextView;
        ImageView imageView;

        //CardView container;
        public MyViewHolder(View view) {
            super(view);
            artisteTextView = (TextView) view.findViewById(R.id.user_name);
            imageView = (ImageView)view.findViewById(R.id.profile_photo);

          //  container = (CardView)view.findViewById(R.id.card_container);

        }
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }



}

