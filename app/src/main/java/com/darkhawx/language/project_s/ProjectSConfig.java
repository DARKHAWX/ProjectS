package com.darkhawx.language.project_s;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;

import com.darkhawx.language.project_s.API.ProjectSAPI;
import com.darkhawx.language.project_s.CardSet.CardSet;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Bradley on 10/11/2017.
 */

public class ProjectSConfig extends Fragment {

    public static final String CONFIG_FILE_NAME = "default-config";
    public static final String INSTALLED_SETS_PATH = "/installedSets/";

    private static String WANIKANI_API_KEY;

    public static void LoadConfig(Application activity) {
        // Load Config
        SharedPreferences settings = activity.getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        WANIKANI_API_KEY = settings.getString("waniKaniApiKey", "");
    }

    public static void SaveConfig(Activity activity) {
        // All objects are from android.context.Context
        SharedPreferences settings = activity.getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("waniKaniApiKey", WANIKANI_API_KEY);

        // Commit the edits!
        editor.apply();
    }

    public static String getWanikaniApiKey() {
        return WANIKANI_API_KEY == null ? "Null" : WANIKANI_API_KEY;
    }

    public static void setWanikaniApiKey(String apikey) {
        WANIKANI_API_KEY = apikey;
    }

    public static void SaveData(Application application) {
        // Obtain sets
        LinkedHashMap<String, CardSet> installedSets = ProjectSAPI.getInstalledSets();

        File dir = application.getApplicationContext().getDir("installedSets", Context.MODE_PRIVATE);

        for (Map.Entry<String, CardSet> entry : installedSets.entrySet()) {
            // Obtain entry values
            String key = entry.getKey();
            CardSet value = entry.getValue();

            // Create file
            File output = new File(dir, key);

            try {
                // Write to file
                OutputStream fos = new FileOutputStream(output, false);
                ObjectOutputStream  oos = new ObjectOutputStream(fos);
                oos.writeObject(value);
                oos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void SaveData(Activity activity) {
        // Obtain sets
        LinkedHashMap<String, CardSet> installedSets = ProjectSAPI.getInstalledSets();

        File dir = activity.getApplicationContext().getDir("installedSets", Context.MODE_PRIVATE);

        for (Map.Entry<String, CardSet> entry : installedSets.entrySet()) {
            // Obtain entry values
            String key = entry.getKey();
            CardSet value = entry.getValue();

            // Create file
            File output = new File(dir, key);

            try {
                // Write to file
                OutputStream fos = new FileOutputStream(output, false);
                ObjectOutputStream  oos = new ObjectOutputStream(fos);
                oos.writeObject(value);
                oos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void LoadData(Application application) {
        // Create hashmap
        LinkedHashMap<String, CardSet> installedSets = new LinkedHashMap<>();

        // Obtain file list
        File dir = new File(INSTALLED_SETS_PATH);
        File[] files = dir.listFiles();

        if (files == null) {
            ProjectSAPI.setInstalledSets(installedSets);
            return;
        }

        for (File f : files) {
            // Obtain set key
            String key = f.getName();

            CardSet value = null;
            try {
                // Attempt to obtain object
                InputStream fis = new FileInputStream(f);
                ObjectInputStream ois = new ObjectInputStream(fis);
                value = (CardSet)ois.readObject();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            if (value != null) {
                installedSets.put(key, value);
            }
        }

        // Save hashmap
        ProjectSAPI.setInstalledSets(installedSets);
    }

    public static void LoadData(Activity activity) {
        // Create hashmap
        LinkedHashMap<String, CardSet> installedSets = new LinkedHashMap<>();

        // Obtain file list
        File dir = new File(INSTALLED_SETS_PATH);
        File[] files = dir.listFiles();

        if (files == null) {
            ProjectSAPI.setInstalledSets(installedSets);
            return;
        }

        for (File f : files) {
            // Obtain set key
            String key = f.getName();

            CardSet value = null;
            try {
                // Attempt to obtain object
                InputStream fis = new FileInputStream(f);
                ObjectInputStream ois = new ObjectInputStream(fis);
                value = (CardSet)ois.readObject();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            if (value != null) {
                installedSets.put(key, value);
            }
        }

        // Save hashmap
        ProjectSAPI.setInstalledSets(installedSets);
    }
}
