package com.darkhawx.language.project_s;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.darkhawx.language.project_s.API.ProjectSAPI;
import com.darkhawx.language.project_s.ExternalIntegration.WaniKaniIntegration.WaniKaniSet;
import com.darkhawx.language.project_s.Overlays.LaunchLoadingActivity;
import com.darkhawx.language.project_s.Overlays.LoadingAppItem;
import com.darkhawx.language.project_s.Overlays.LoadingAppSelectionActivity;
import com.darkhawx.language.project_s.Reviews.ReviewSetActivity;
import com.darkhawx.language.project_s.Test.DisplayMessageActivity;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.darkhawx.language.project_s.MESSAGE";
    public static final String SET_ID_MESSAGE = "com.darkhawx.language.project_s.SET_ID";

    public static final String DARKHAWX_API_V1_KEY = "09246629ea90a014b6c99bf66399f5c8";
    public static final String DARKHAWX_API_V2_KEY = "9a9305d7-302d-4f76-afe7-11a23b948c7f";

    public final static int REQUEST_CODE = 10101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((TextView) findViewById(R.id.waniKaniKeyText)).setText(getString(R.string.wanikani_api_key, ProjectSConfig.getWanikaniApiKey()));
        ((TextView) findViewById(R.id.numSets)).setText(getString(R.string.num_sets, ProjectSAPI.numInstalledSets()));

        if (!Settings.canDrawOverlays(this)) {
            /** if not construct intent to request permission */
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            /** request permission via start activity for result */
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        ProjectSConfig.SaveConfig(this);
        ProjectSConfig.SaveData(this);
    }


    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void setWaniKani(View view) {
        ProjectSConfig.setWanikaniApiKey(((EditText)findViewById(R.id.waniKaniKeyTextArea)).getText().toString());
        ProjectSConfig.SaveConfig(this);
        ((TextView)findViewById(R.id.waniKaniKeyText)).setText(getString(R.string.wanikani_api_key, ProjectSConfig.getWanikaniApiKey()));
    }

    public void testSet(View view) {
        Intent intent = new Intent(this, ReviewSetActivity.class);
        intent.putExtra(SET_ID_MESSAGE,  ProjectSAPI.getCardSet("test"));
        startActivity(intent);
    }

    public void waniKaniSet(View view) {
        WaniKaniSet waniKani = (WaniKaniSet) ProjectSAPI.getCardSet("WaniKani");
        if (waniKani != null) {
            Intent intent = new Intent(this, ReviewSetActivity.class);
            intent.putExtra(SET_ID_MESSAGE, waniKani.getReviewSet());
            startActivity(intent);
        }
    }

    public void addSets(View view) {
        if (ProjectSConfig.getWanikaniApiKey() != null) {
            WaniKaniSet waniKaniSet = new WaniKaniSet();
            waniKaniSet.updateCachedData();

            ProjectSAPI.addCardSet("WaniKani", waniKaniSet);
        }

        ProjectSAPI.addTestSets();
    }

    public void save(View view) {
        ProjectSConfig.SaveData(this);
        Log.i("Project_S.MainActivity", "Saved Data");
    }

    public void load(View view) {
        ProjectSConfig.LoadData(this);
        ((TextView) findViewById(R.id.numSets)).setText(getString(R.string.num_sets, ProjectSAPI.numInstalledSets()));
        Log.i("Project_S.MainActivity", "Loaded Data");
    }

    public void openLoadingButtons(View view) {
        Intent intent = new Intent(this, LoadingAppSelectionActivity.class);
        startActivity(intent);
    }

    public void testShortcut(View view) {
        Intent shortcutIntent = new Intent(getApplicationContext(), MainActivity.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Test");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.mipmap.ic_launcher));
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);
    }

    public static void testShortcuttest(LoadingAppItem item, Context context) {
        Intent shortcutIntent = new Intent(context.getApplicationContext(), LaunchLoadingActivity.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        shortcutIntent.putExtra(LaunchLoadingActivity.APPLICATION_INFO_MESSAGE, item.getInfo().packageName);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, String.valueOf(context.getPackageManager().getApplicationLabel(item.getInfo())));
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, item.getInfo().icon);
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        context.getApplicationContext().sendBroadcast(addIntent);
    }
}
