package com.github.p1va.typoapp.utils;

import android.os.Environment;

import java.util.UUID;

/**
 * Created by Stefano Piva on 19/08/2019.
 */
public class StorageUtils {
    /**
     * Checks if the external storage is writable
     * @return The flag describing if storage is writable or not
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /**
     * Checks if the external storage readable
     * @return The flag describing if storage is readable
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    /**
     * Gets the external storage path
     * @return The external storage path
     */
    public static String getExternalStoragePath(){
        String externalStoragePath = Environment
                .getExternalStorageDirectory()
                .toString();

        return externalStoragePath;
    }

    /**
     * Gets a random file name with the specified extension
     * @param fileExtension The file extension without the dot
     * @return The random file name
     */
    public static String getRandomFileName(String fileExtension){
        String uniqueString = UUID.randomUUID().toString().replace("-", "");

        String randomFileName = uniqueString + "." + fileExtension;

        return randomFileName;
    }
}
