package com.darkhawx.language.project_s.Overlays;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.darkhawx.language.project_s.R;

import java.util.List;

public class LoadingAppSelectionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_app_selection);

        final PackageManager pm = getPackageManager();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        LoadingAppItem[] items = new LoadingAppItem[packages.size()];
        for (int i = 0; i < packages.size(); i++) {
            items[i] = new LoadingAppItem(packages.get(i));
        }

        final ListView listview = (ListView) findViewById(R.id.list);
        final LoadingAppAdapter appAdapter = new LoadingAppAdapter(this, items);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                final LoadingAppItem item = (LoadingAppItem)parent.getItemAtPosition(position);
                final String appName = String.valueOf(getPackageManager().getApplicationLabel(item.info));

                Intent launchIntent = new Intent(getApplicationContext(), LaunchLoadingActivity.class);
                launchIntent.setAction(Intent.ACTION_MAIN);
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                Intent intent = new Intent();
                intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launchIntent);
                intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName + "-S");
                intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, item.getIcon());
                launchIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
                getApplicationContext().sendBroadcast(launchIntent);
            }
        });

        listview.setAdapter(appAdapter);
    }
}
