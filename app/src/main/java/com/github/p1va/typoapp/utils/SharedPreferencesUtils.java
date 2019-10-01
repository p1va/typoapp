package com.github.p1va.typoapp.utils;

import android.content.Context;

import timber.log.Timber;

/**
 * Created by Stefano Piva on 28/09/2019.
 */
public class SharedPreferencesUtils {

    /**
     * The string default value
     */
    private static final String STRING_DEFAULT_VALUE = null;

    /**
     * Gets the value of a shared preference key
     *
     * @param context The context
     * @param key     The key to access
     * @return The value of the shared preference
     */
    public static String get(Context context, String key) {
        String value = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE)
                .getString(key, STRING_DEFAULT_VALUE);

        Timber.d("Getting key:" + key + ". Found value: " + value);

        return value;
    }

    /**
     * Sets the value of a shared preference key
     *
     * @param context The context
     * @param key     The key to set
     * @param value   The value to set
     * @return A flag describing outcome of set operation
     */
    public static boolean set(Context context, String key, String value) {

        Timber.d("Setting key:" + key + " to value: " + value);

        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).edit()
                .putString(key, value).commit();
    }
}
