package com.twisstosin.andela_alc_challenge;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.twisstosin.andela_alc_challenge.Adapters.CustomRecyclerAdapter;
import com.twisstosin.andela_alc_challenge.Adapters.RecyclerItemListener;
import com.twisstosin.andela_alc_challenge.Models.GitHubUser;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager linearLayoutManager;
    CustomRecyclerAdapter aud;
    TextView emptyListText;
    ProgressBar loadMore;
    List<GitHubUser> users = new ArrayList<>();

    Boolean refing = false;

    JSONParser jsonParser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emptyListText = (TextView)findViewById(R.id.empty_users_text);
        recyclerView = (RecyclerView)findViewById(R.id.list_view);
        loadMore = (ProgressBar)findViewById(R.id.progress_bar);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        //Initializing empty recyclerview for refreshlayout to work properly
        aud = new CustomRecyclerAdapter(this,users);
        recyclerView.setAdapter(aud);

        //Initializing JSONParser
        jsonParser = new JSONParser();

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refresher);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(isLastItemDisplaying(recyclerView)) //check for scroll down
                {
                    loadMoreRefresh();
                }
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemListener(this, recyclerView, new RecyclerItemListener.RecyclerTouchListener() {
                    @Override
                    public void onClickItem(View v, final int position) {

                        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, R.style.AppTheme_Dark_Dialog);
                        progressDialog.setMessage("Getting Profile");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        AsyncTask<Void, GitHubUser, GitHubUser> task = new AsyncTask<Void, GitHubUser, GitHubUser>(){

                            @Override
                            protected GitHubUser doInBackground(Void... params) {
                                GitHubUser response = null;
                                try {
                                    response = jsonParser.getProfile(users.get(position));

                                } catch (final Exception e) {
                                    //createAndShowDialogFromTask(e, "Error");
                                    new Dialog(MainActivity.this).setTitle(e.getMessage());
                                }

                                return response;
                            }

                            @Override
                            protected void onPostExecute(GitHubUser result) {
                                progressDialog.dismiss();
                                if(result != null)
                                {
                                    Gson gson = new Gson();
                                    String user = gson.toJson(result);
                                    Intent intent = new Intent(MainActivity.this,ProfileActivity.class);

                                    intent.putExtra(ApiData.INTENT_USER,user);
                                    startActivity(intent);

                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this, "Bad Internet Connection", Toast.LENGTH_SHORT).show();
                                }
                            }
                        };
                        runAsyncTask(task);
                    }

                    @Override
                    public void onLongClickItem(View v, int position) {

                    }
                }));

            swipeRefresh();
    }



    private AsyncTask<Void, GitHubUser, GitHubUser> runAsyncTask(AsyncTask<Void, GitHubUser, GitHubUser> task) {
        return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void swipeRefresh()
    {
        refing = true;
        users.clear();
        jsonParser.resetPageCount();
        emptyListText.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(true);
        refreshItemsFromTable();
    }

    private void loadMoreRefresh()
    {
        refing = false;
        loadMore.setVisibility(View.VISIBLE);
        if(jsonParser.isListEnd()) {
            Toast.makeText(MainActivity.this, "End of List", Toast.LENGTH_SHORT).show();
            loadMore.setVisibility(View.GONE);
        }
        else
        {
            refreshItemsFromTable();
        }
    }

    private void refreshItemsFromTable() {
        Log.d("JSONPack","Started Refresh");

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                List<GitHubUser> gitHubUserList = null;
                try {
                    gitHubUserList = jsonParser.getUsers();
                    Log.d("JSONPack","Tried to get users");
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                    Log.d("JSONPack","Exeption");
                }

                final List<GitHubUser> finalGitHubUserList = gitHubUserList;
                runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(finalGitHubUserList != null) {
                                if(finalGitHubUserList.size()!=0)
                                {
                                    if(users != null)
                                    users.addAll(finalGitHubUserList);
                                    else
                                        users = finalGitHubUserList;

                                    //aud = new CustomRecyclerAdapter(MainActivity.this, users);
                                    aud.notifyDataSetChanged();
                                    //recyclerView.setAdapter(aud);
                                    swipeRefreshLayout.setRefreshing(false);
                                    loadMore.setVisibility(View.GONE);
                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this, "No Users to Display", Toast.LENGTH_SHORT).show();
                                    //refreshLayout.setVisibility(View.GONE);
                                    swipeRefreshLayout.setRefreshing(false);
                                    loadMore.setVisibility(View.GONE);
                                }
                            }
                            else {
                                if(refing)
                                {
                                    Toast.makeText(MainActivity.this, "Null", Toast.LENGTH_SHORT).show();
                                    emptyListText.setVisibility(View.VISIBLE);
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                                else
                                loadMore.setVisibility(View.GONE);
                            }
                        }
                    });

                return null;
            }
    };
        runAsyncTask(task,"");
    }


    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }


    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task,String id) {
        return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if(exception.getCause() != null){
            ex = exception.getCause();
        }
        createAndShowDialog(ex.getMessage()+"\nCheck your internet connection", title);
    }

    private void createAndShowDialog(final String message, final String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }

    private void createAndShowDialogFromTask(final Exception exception) {
        createAndShowDialog(exception, "Could not refresh");
    }
}
