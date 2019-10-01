package com.github.p1va.typoapp.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;

import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;

/***
 * The images utils class
 */
public class ImagesUtils {

    /***
     * Inserts an image in the media store
     * @param contentResolver The content resolver
     * @param bitmap The bitmap that needs to be saved
     * @param title The title that will be used to save the image
     * @param description The description of the image
     * @return The content path of the image returned by the media store
     * @throws Exception An exception in case of failure
     */
    @NotNull
    public static final Uri insertImage(ContentResolver contentResolver, Bitmap bitmap, String title, String description) throws Exception {

        // Declare content values
        ContentValues v = new ContentValues();
        v.put(Images.Media.TITLE, title);
        v.put(Images.Media.DISPLAY_NAME, title);
        v.put(Images.Media.DESCRIPTION, description);
        //v.put(Images.Media.BUCKET_ID, "");
        //v.put(Images.Media.BUCKET_DISPLAY_NAME, "");
        v.put(Images.Media.MIME_TYPE, "image/jpeg");
        v.put(Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        v.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());

        // Insert the image using the media store
        Uri imageContentPath = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, v);

        try {

            // Open an output stream using the content URI
            OutputStream outputStream = contentResolver.openOutputStream(imageContentPath);

            // Compress the image into the output stream
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            // Flush and close the stream
            outputStream.flush();
            outputStream.close();

        } catch (Exception e) {

            // Check if we were able to create a record in the media store
            if(imageContentPath != null) {

                // Remove the image from the media store since we failed saving it
                contentResolver.delete(imageContentPath, null, null);
            }

            throw new Exception("An error occurred saving the image in the media store", e);
        }

        // Everything went fine, returning path
        return imageContentPath;
    }
}