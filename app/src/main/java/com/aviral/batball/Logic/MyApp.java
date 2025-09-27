package com.aviral.batball.Logic;

import android.app.Application;

import com.cloudinary.android.MediaManager;
import java.util.HashMap;
import java.util.Map;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Map config = new HashMap();
        config.put("cloud_name", "dxbfzyc0f"); // from Cloudinary dashboard
        MediaManager.init(this, config);
    }
}
