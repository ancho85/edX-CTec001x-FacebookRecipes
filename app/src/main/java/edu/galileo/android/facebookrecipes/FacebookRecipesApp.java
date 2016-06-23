package edu.galileo.android.facebookrecipes;

import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * Created by carlos.gomez on 22/06/2016.
 */
public class FacebookRecipesApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initFacebook();
    }

    private void initFacebook() {
        FacebookSdk.sdkInitialize(this);
    }
}
