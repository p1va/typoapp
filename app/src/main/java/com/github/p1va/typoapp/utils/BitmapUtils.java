package com.github.p1va.typoapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by Stefano Piva on 10/05/2019.
 */
public class BitmapUtils {
    /**
     * Capture the view into a bitmap
     *
     * @param view The view to capture
     * @return The bitmap representing the view
     */
    public static Bitmap captureViewToBitmap(View view) {

        // Create the bitmap with the same size of the view
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);

        // Create a canvas
        Canvas canvas = new Canvas(bitmap);

        // Draw the view on the canvas
        view.draw(canvas);

        // Return the bitmap
        return bitmap;
    }

    public static Drawable resize(Context context, Drawable image, int dst) {
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, dst, dst, false);
        return new BitmapDrawable(context.getResources(), bitmapResized);
    }

    public static int convertDpToPx(Context context, float dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float pixels = metrics.density * dp;
        return (int) (pixels + 0.5f);
    }
}
