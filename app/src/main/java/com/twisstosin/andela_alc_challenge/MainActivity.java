package com.twisstosin.andela_alc_challenge;

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
import android.widget.TextView;
import android.widget.Toast;

import com.twisstosin.andela_alc_challenge.Adapters.CustomRecyclerAdapter;
import com.twisstosin.andela_alc_challenge.Adapters.RecyclerItemListener;
import com.twisstosin.andela_alc_challenge.Models.GitHubUser;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager linearLayoutManager;
    CustomRecyclerAdapter aud;
    TextView emptyListText;
    List<GitHubUser> users = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emptyListText = (TextView)findViewById(R.id.empty_users_text);
        recyclerView = (RecyclerView)findViewById(R.id.list_view);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refresher);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItemsFromTable();
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemListener(this, recyclerView, new RecyclerItemListener.RecyclerTouchListener() {
                    @Override
                    public void onClickItem(View v, int position) {
                        startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                    }

                    @Override
                    public void onLongClickItem(View v, int position) {

                    }
                }));

            refreshItemsFromTable();
    }

    private void refreshItemsFromTable() {
        Log.d("JSONPack","Started Refresh");
        emptyListText.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(true);
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                    JSONParser jsonParser = new JSONParser();
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
                                    users = finalGitHubUserList;
                                    aud = new CustomRecyclerAdapter(MainActivity.this, users);

                                    recyclerView.setAdapter(aud);
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this, "No Users to Display", Toast.LENGTH_SHORT).show();
                                    //refreshLayout.setVisibility(View.GONE);
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            }
                            else {
                                Toast.makeText(MainActivity.this, "Null", Toast.LENGTH_SHORT).show();
                                emptyListText.setVisibility(View.VISIBLE);
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    });

                return null;
            }
    };
        runAsyncTask(task);
    }


    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
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
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
        createAndShowDialog(exception, "Could not refresh");
//                refreshLayout.setRefreshing(false);
//                if(noteList == null)
//                {
//                    emptyListText.setVisibility(View.VISIBLE);
//                }
    }
}
