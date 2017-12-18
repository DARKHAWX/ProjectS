package com.darkhawx.language.project_s;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;

import com.darkhawx.language.project_s.ExternalIntegration.WaniKaniIntegration.WaniKaniAccount;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Bradley on 10/11/2017.
 */

public class ProjectSConfig extends Fragment {

    public static final String CONFIG_FILE_NAME = "default-config";
    public static final String WANIKANI_FILE_NAME = "wanikani-account";

    //private static String WANIKANI_API_KEY;
    public static WaniKaniAccount WANIKANI_ACCOUNT;

    public static void LoadConfig(Activity activity) {
        // Load Config
        SharedPreferences settings = activity.getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
//        WANIKANI_API_KEY = settings.getString("waniKaniApiKey", "");

        // Load WaniKani Account
        FileInputStream wk_fis = null;
        try {
            wk_fis = activity.openFileInput(WANIKANI_FILE_NAME);
            ObjectInputStream wk_ois = new ObjectInputStream(wk_fis);
            WANIKANI_ACCOUNT = (WaniKaniAccount) wk_ois.readObject();
            wk_ois.close();
            wk_fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (WANIKANI_ACCOUNT == null) {
            WANIKANI_ACCOUNT = new WaniKaniAccount("Null", "Null");
        }

    }

    public static void SaveConfig(Activity activity) {
        // All objects are from android.context.Context
        SharedPreferences settings = activity.getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
//        editor.putString("waniKaniApiKey", WANIKANI_API_KEY);

        // Commit the edits!
        editor.apply();

        // Save WaniKani Account
        FileOutputStream fos = null;
        try {
            fos = activity.openFileOutput(WANIKANI_FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(WANIKANI_ACCOUNT);
            os.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
