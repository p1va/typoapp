package com.github.p1va.typoapp.utils;

import android.content.Context;
import android.view.Gravity;

import com.github.p1va.typoapp.models.Theme;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by Stefano Piva on 05/05/2019.
 */
public class ThemesUtils {

    public static ArrayList<Theme> loadFromResources(Context context, String fileName) {

        String fileContet = readAssetFile(context, fileName);
        if(fileContet == null) {

            Timber.e("Failed loading asset file named " + fileName);

            // File was not found
            return null;
        }

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        try {
            return gson.fromJson(fileContet, new TypeToken<ArrayList<Theme>>() {
            }.getType());
        } catch (Exception ex) {

            Timber.e(ex, "Failed deserializing list of themes from JSON");

            return null;
        }
    }

    private static String readAssetFile(Context context, String fileName) {

        BufferedReader reader = null;

        try {

            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(fileName), "UTF-8"));

            //
            String line;
            String content = "";
            long length = 0;

            // Loop through all of the lines
            while ((line = reader.readLine()) != null) {
                length += line.length();
                content += line;
            }

            return content;
        } catch (IOException e) {
            //log the exception

        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }

        return null;
    }


    public static int getHorizontalGravity(String alignment) {
        switch (alignment) {
            case "center":
                return Gravity.CENTER_HORIZONTAL;

            case "left":
                return Gravity.START;

            case "right":
                return Gravity.END;
            default:
                return Gravity.CENTER_HORIZONTAL;
        }
    }

    public static int getVerticalGravity(String alignment) {
        switch (alignment) {
            case "center":
                return Gravity.CENTER_VERTICAL;

            case "bottom":
                return Gravity.BOTTOM;

            case "top":
                return Gravity.TOP;
            default:
                return Gravity.CENTER_VERTICAL;
        }
    }
}
