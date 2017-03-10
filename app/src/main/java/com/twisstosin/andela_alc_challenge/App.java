package com.twisstosin.andela_alc_challenge;

import android.app.Application;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;


/**
 * Created by twisstosin on 3/5/2017.
 */

public class App extends Application {

    @Override
    public void onCreate()
    {
        super.onCreate();
    //    Toast.makeText(this, "App Started", Toast.LENGTH_SHORT).show();

        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/gothic.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/gothic.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/gothic.ttf");

    }

    public static AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task, String id)
    {
        return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void createAndShowDialog(Exception exception, String title)
    {
        Throwable ex = exception;
        if(exception.getCause() != null){
            ex = exception.getCause();
        }
        createAndShowDialog(ex.getMessage()+"\nCheck your internet connection", title);
    }

    private void createAndShowDialog(final String message, final String title)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }

    private void createAndShowDialogFromTask(final Exception exception)
    {
        createAndShowDialog(exception, "Could not refresh");
    }
}
