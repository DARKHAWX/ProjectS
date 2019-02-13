package com.darkhawx.language.project_s;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Bradley Gray on 11-Feb-18.
 */

public class Tools {

    public static void exceptionToast(Context context, String message) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
}
