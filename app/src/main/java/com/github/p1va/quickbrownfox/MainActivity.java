package com.github.p1va.quickbrownfox;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.DynamicLayout;
import android.text.Editable;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.florent37.longshadow.LongShadow;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ThemesAdapter.ItemClickListener {

    private static final int IMAGE_WIDTH_PX = 1080;
    private static final int IMAGE_HEIGHT_PX = 1080;
    //private ImageView mImageView;
    private EditText mEditText;
    private LongShadow mLongShadow;

    private ArrayList<Theme> themes;

    private Bitmap mBitmap;

    @Override
    public void onItemClick(View view, int position) {

        // Retrieve theme
        Theme theme = themes.get(position);

        // Apply it
        applyTheme(theme);
    }

    private Drawable resize(Drawable image, int dst) {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, dst, dst, false);
        return new BitmapDrawable(getResources(), bitmapResized);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set support toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.main_toolbar));

        // Set icon
       //getSupportActionBar().setIcon(R.drawable.fox);

        Drawable foxDrawable = getResources().getDrawable(R.drawable.fox);

        getSupportActionBar().setLogo(resize(foxDrawable, 90));
        getSupportActionBar().setElevation(0);


        //mLongShadow = findViewById(R.id.shadow);

        mEditText = findViewById(R.id.edit_text);

        applyTheme(new Theme("Montserrat", "Montserrat-Bold.ttf", 15, convertResourceToColor(R.color.beach_font_color), convertResourceToColor(R.color.beach_bg)));

        mEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBitmap = createBitmap(s.toString());

                //mImageView.setImageBitmap(mBitmap);

                //saveBitmapToFile(mBitmap, "test.png");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        // data to populate the RecyclerView with
        themes = new ArrayList<>();
        themes.add(new Theme("Montserrat Dark", "Montserrat-Bold.ttf", 10, convertResourceToColor(R.color.beach_font_color), convertResourceToColor(R.color.beach_bg)));
        themes.add(new Theme("Montserrat Original", "Montserrat-Bold.ttf", 10, convertResourceToColor(R.color.montserrat_font_color), convertResourceToColor(R.color.montserrat_bg)));
        themes.add(new Theme("Montserrat Beach", "Montserrat-Bold.ttf", 10, convertResourceToColor(R.color.beach_font_color2), convertResourceToColor(R.color.beach_bg2)));
        themes.add(new Theme("A book", "EBGaramond-Regular.ttf", 10, convertResourceToColor(R.color.beach_bg), convertResourceToColor(R.color.beach_bg3)));
        themes.add(new Theme("Amatic Dark", "AmaticSC-Regular.ttf", 10, convertResourceToColor(R.color.beach_bg3), convertResourceToColor(R.color.beach_bg)));
        themes.add(new Theme("Amatic Light", "AmaticSC-Regular.ttf", 10, convertResourceToColor(R.color.beach_bg), convertResourceToColor(R.color.beach_bg3)));
        themes.add(new Theme("VCR", "VCR_OSD_MONO.ttf", 10, convertResourceToColor(R.color.theme_vcr_ft), convertResourceToColor(R.color.theme_vcr_bg)));
        themes.add(new Theme("Playfair", "PlayfairDisplay-Italic.ttf", 10, convertResourceToColor(R.color.theme_playfare_ft), convertResourceToColor(R.color.theme_playfare_bg)));
        themes.add(new Theme("Quicksand", "Quicksand-Regular.ttf", 10, convertResourceToColor(R.color.theme_quicksand_ft), convertResourceToColor(R.color.theme_quicksand_bg)));


        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.themes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ThemesAdapter adapter = new ThemesAdapter(this, themes);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());

        // Path will be /data/data/yourapp/app_data/images
        File directory = cw.getDir("images", Context.MODE_PRIVATE);

        // Create images
        File path = new File(directory, "test.png");

        FileOutputStream fos = null;

        try {

            fos = new FileOutputStream(path);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    final int REQUEST = 112;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //do here
                } else {
                    Toast.makeText(this, "The app was not allowed to write in your storage", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if(ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void shareImage(Bitmap bitmap) {

        if(Build.VERSION.SDK_INT >= 23) {

            String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

            if(!hasPermissions(this, PERMISSIONS)) {

                ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST);
            } else {
                //do here

                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "test.png", null);

                Uri contentUri = Uri.parse(path);

                Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                        .setType("image/png")
                        .setStream(contentUri)
                        .getIntent();

                shareIntent.setData(contentUri);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                startActivity(Intent.createChooser(shareIntent, "Share"));
            }
        } else {
            //do here
        }
    }

    private void sendImage(Bitmap bitmap) {
        try {

            String imagePath = saveToInternalStorage(bitmap);

            Log.d("sendImage", "Image path is: " + imagePath);

            Uri imageUri = Uri.parse(imagePath);

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("image/png");
            waIntent.putExtra(android.content.Intent.EXTRA_STREAM, imageUri);
            waIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            this.startActivity(Intent.createChooser(waIntent, "Share with"));
        } catch (Exception e) {
            Log.e("Error on sharing", e + " ");
            Toast.makeText(this, "App not Installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveBitmapToFile(Bitmap bitmap, String name) {
        FileOutputStream out = null;

        try {

            out = new FileOutputStream(name);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int convertResourceToColor(int resourceId){
        return ResourcesCompat.getColor(getResources(), resourceId, null);
    }

    private void applyTheme(Theme theme) {

        // Get typeface
        AssetManager am = getApplicationContext().getAssets();
        Typeface montserratBold = Typeface.createFromAsset(am, String.format(Locale.US, "fonts/%s", theme.font));

        mEditText.setBackgroundColor(theme.backgroundColor);
        mEditText.setTextColor(theme.fontColor);
        mEditText.setTypeface(montserratBold);

        int padding = convertDpToPx(16);

        mEditText.setPadding(padding, padding,padding,padding);


        mEditText.setTextSize(convertDpToPx(theme.fontSize));

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mEditText.setLetterSpacing(0.15f);
        }
    }

    private int convertDpToPx(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float pixels = metrics.density * dp;
        return (int) (pixels + 0.5f);
    }

    private Bitmap createBitmap(String text) {
        try {

            // Retrieve display scale
            float scale = this.getResources().getDisplayMetrics().density;

            // Retrieve font and background color
            //int fontColor = ResourcesCompat.getColor(getResources(), R.color.montserrat_font_color, null);
            //int backgroundColor = ResourcesCompat.getColor(getResources(), R.color.montserrat_bg, null);

            int fontColor = ResourcesCompat.getColor(getResources(), R.color.beach_font_color, null);
            int backgroundColor = ResourcesCompat.getColor(getResources(), R.color.beach_bg, null);

            // Get typeface
            AssetManager am = getApplicationContext().getAssets();
            Typeface montserratBold = Typeface.createFromAsset(am, String.format(Locale.US, "fonts/%s", "Montserrat-Bold.ttf"));

            // Declare bitmap
            Bitmap.Config conf = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = Bitmap.createBitmap(IMAGE_WIDTH_PX, IMAGE_HEIGHT_PX, conf);

            // Declare canvas from bitmap
            Canvas canvas = new Canvas(bitmap);

            // Set background
            Paint backgroundPaint = new Paint();
            backgroundPaint.setStyle(Paint.Style.FILL);
            backgroundPaint.setColor(backgroundColor);
            canvas.drawPaint(backgroundPaint);

            // Set text
            TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            textPaint.setColor(fontColor);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textPaint.setLetterSpacing(0.15f);
            }

            textPaint.setTypeface(montserratBold);
            textPaint.setTextSize((int) (30 * scale));

            canvas.save();

            DynamicLayout dynamicLayout = new DynamicLayout(text, textPaint,
                    1080, Layout.Alignment.ALIGN_CENTER,
                    1.0f, 1.0f, true);

            dynamicLayout.draw(canvas);

            canvas.restore();

            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }
}
