package com.darkhawx.language.project_s.Overlays;

import android.content.pm.ApplicationInfo;

/**
 * Created by Bradley Gray on 15-Jan-18.
 */

public class LoadingAppItem {

    ApplicationInfo info;

    LoadingAppItem(ApplicationInfo info) {
        this.info = info;
    }

    public int getIcon() {
        return info.icon;
    }

    public ApplicationInfo getInfo() {
        return info;
    }
}
