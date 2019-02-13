package com.darkhawx.language.project_s.Overlays;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.darkhawx.language.project_s.CardSet.CardSet;
import com.darkhawx.language.project_s.MainActivity;
import com.darkhawx.language.project_s.R;
import com.darkhawx.language.project_s.Tools;

public class LaunchLoadingActivity extends AppCompatActivity {

    public static final String APPLICATION_INFO_MESSAGE = "com.darkhawx.language.project_s.overlays.APPLICATION_INFO_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_loading);

        Intent intent = getPackageManager().getLaunchIntentForPackage(getIntent().getStringExtra(APPLICATION_INFO_MESSAGE));
        if (intent != null) {
            startActivity(intent);

            startService(new Intent(getApplicationContext(), LoadingOverlayService.class));
            finish();
        } else {
            Tools.exceptionToast(this, "Error Launching Application Overlay");
        }
    }
}
