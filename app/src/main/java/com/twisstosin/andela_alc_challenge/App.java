package com.twisstosin.andela_alc_challenge;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by twisstosin on 3/5/2017.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    //    Toast.makeText(this, "App Started", Toast.LENGTH_SHORT).show();

        //FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/proximanova_regular.ttf");
        //FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/proximanova_regular.ttf");
        //FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/proximanova_regular.ttf");
        Fresco.initialize(this);

    }
}