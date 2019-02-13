package com.darkhawx.language.project_s;

import android.app.Application;

import com.darkhawx.language.project_s.API.ProjectSAPI;

/**
 * Created by Bradley Gray on 05-Jan-18.
 */

public class ProjectSApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        ProjectSConfig.LoadConfig(this);
        ProjectSConfig.LoadData(this);
    }
}
