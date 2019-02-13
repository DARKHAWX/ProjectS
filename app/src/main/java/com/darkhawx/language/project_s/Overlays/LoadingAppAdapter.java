package com.darkhawx.language.project_s.Overlays;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.darkhawx.language.project_s.MainActivity;
import com.darkhawx.language.project_s.R;

/**
 * Created by Bradley Gray on 15-Jan-18.
 */

public class LoadingAppAdapter extends ArrayAdapter<LoadingAppItem> {

    private LoadingAppItem[] items = null;

    public LoadingAppAdapter(Context context, LoadingAppItem[] resource) {
        super(context, R.layout.loading_app_selection_list_item, resource);
        this.items = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();
        convertView = inflater.inflate(R.layout.loading_app_selection_list_item, parent, false);


        ImageView icon = (ImageView) convertView.findViewById(R.id.appIcon);
        TextView name = (TextView) convertView.findViewById(R.id.appName);
        Button button = (Button) convertView.findViewById(R.id.addShortcutButton);
        button.setText("Add");

        final LoadingAppItem item = items[position];
        final String appName = String.valueOf(getContext().getPackageManager().getApplicationLabel(item.info));
        try {
            Drawable d = getContext().getPackageManager().getApplicationIcon(item.info.packageName);
            icon.setImageDrawable(d);

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(this.getClass().getSimpleName(), "NameNotFoundException: " + item.info.packageName);
        }

        name.setText(appName);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.testShortcuttest(item, getContext());
//                Intent launchIntent = new Intent(getContext().getApplicationContext(), LaunchLoadingActivity.class);
//                launchIntent.setAction(Intent.ACTION_MAIN);
//                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                Intent intent = new Intent();
//                intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launchIntent);
//                intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName + "-S");
//                intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, item.getIcon());
//                launchIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
//                getContext().getApplicationContext().sendBroadcast(launchIntent);
            }
        });

        return convertView;
    }
}

