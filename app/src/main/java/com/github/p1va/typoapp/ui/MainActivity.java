package com.github.p1va.typoapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.text.emoji.EmojiCompat;
import android.support.text.emoji.bundled.BundledEmojiCompatConfig;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.p1va.typoapp.Constants;
import com.github.p1va.typoapp.R;
import com.github.p1va.typoapp.adapters.ThemesAdapter;
import com.github.p1va.typoapp.adapters.ViewPagerAdapter;
import com.github.p1va.typoapp.models.Theme;
import com.github.p1va.typoapp.utils.BitmapUtils;
import com.github.p1va.typoapp.utils.ImagesUtils;
import com.github.p1va.typoapp.utils.ListsUtils;
import com.github.p1va.typoapp.utils.SharedPreferencesUtils;
import com.github.p1va.typoapp.utils.StorageUtils;
import com.github.p1va.typoapp.utils.ThemesUtils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Locale;

import timber.log.Timber;

/**
 * The main activity
 */
public class MainActivity extends AppCompatActivity implements
        ThemesAdapter.ItemClickListener,
        ThemesFragment.OnFragmentInteractionListener,
        TextSettingsFragment.OnFragmentInteractionListener,
        TextAlignmentFragment.OnFragmentInteractionListener {

    /**
     * The request code
     */
    private final int REQUEST = 112;

    /**
     * The permissions to check
     */
    private final String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * The main edit text
     */
    private AppCompatEditText mEditText;

    /**
     * The array list of all of the available mThemes
     */
    private ArrayList<Theme> mThemes;

    /**
     * The exported bitmap
     */
    private Bitmap mBitmap;

    /**
     * The selected theme
     */
    private Theme mTheme;

    /**
     * The share button
     */
    private Button mButtonShare;

    /**
     * The themes fragment
     */
    private ThemesFragment mThemesFragment;

    /**
     * The text settings fragment
     */
    private TextSettingsFragment mTextSettingsFragment;

    /**
     * The text alignment fragment
     */
    private TextAlignmentFragment mTextAlignmentFragment;

    /**
     * The item click listener
     *
     * @param view     The view
     * @param position The position
     */
    @Override
    public void onItemClick(View view, int position) {

        Timber.d("Theme at position " + position + " has been clicked.");

        // Apply ch@anges
        mTheme = mThemes.get(position);

        // Apply it
        applySelectedTheme();
    }

    /**
     * Called when activity is created
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize emoji compact
        EmojiCompat.Config emojiConfig = new BundledEmojiCompatConfig(this);
        emojiConfig.setReplaceAll(true);
        emojiConfig.setUseEmojiAsDefaultStyle(true);
        EmojiCompat.init(emojiConfig);

        setContentView(R.layout.activity_main);

        // Try to retrieve any previously entered text value
        String textToSet = SharedPreferencesUtils.get(this, Constants.KEY_PREVIOUS_TEXT_VALUE);

        // Check if anything was found in the preferences
        if(textToSet == null) {

            // Set the text to its default
            textToSet = getResources()
                    .getString(R.string.default_welcome_text);
        }

        // Get main edit text
        mEditText = findViewById(R.id.edit_text);
        mEditText.setText(textToSet);
        mEditText.setSelection(mEditText.getText().length());
        mEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Get the viewpager
        NonSwipeableViewPager viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tabs);

        // Declare adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Load mThemes from assets
        mThemes = ThemesUtils.loadFromResources(this, "themes.json");

        Timber.d("Themes from json: " + mThemes.size());

        // Declare the fragments
        mThemesFragment = ThemesFragment.newInstance(mThemes);
        mTextSettingsFragment = TextSettingsFragment.newInstance();
        mTextAlignmentFragment = TextAlignmentFragment.newInstance();

        // Add fragments
        adapter.addFragment(mThemesFragment);
        adapter.addFragment(mTextSettingsFragment);
        adapter.addFragment(mTextAlignmentFragment);

        // Set the adapter
        viewPager.setAdapter(adapter);

        // Set the tab layout
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.baseline_format_paint_white_36dp);
        tabLayout.getTabAt(1).setIcon(R.mipmap.baseline_format_size_white_36dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.baseline_format_align_center_white_36dp);

        // Initialize the share button
        mButtonShare = findViewById(R.id.share_button);
        mButtonShare.setOnClickListener(new View.OnClickListener() {

            /**
             * Handles the on click event on the share button
             * @param v The button view
             */
            @Override
            public void onClick(View v) {

                // Remove focus
                mEditText.setCursorVisible(false);
                mEditText.clearFocus();
                mEditText.clearComposingText();

                // Capture edit text to bitmap
                mBitmap = BitmapUtils.captureViewToBitmap(mEditText);

                // Ensure permissions are granted and share
                ensurePermissionAndShareImage();

                // Re enable cursor
                mEditText.setCursorVisible(true);

                // Save the text value in the preferences
                SharedPreferencesUtils.set(getApplicationContext(), Constants.KEY_PREVIOUS_TEXT_VALUE, mEditText.getText().toString());
            }
        });

        // Pick random one
        mTheme = ListsUtils.pickRandomItem(mThemes);

        // Apply random one
        applySelectedTheme();
    }

    /**
     * The on request permissions result callback method
     *
     * @param requestCode  The request code
     * @param permissions  The permissions to check
     * @param grantResults The grant results
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Timber.d("on request permission result called");

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case REQUEST: {

                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Timber.d("Permission to write external storage was granted");

                    // Save and share the image
                    saveAndShareImage();
                } else {

                    Toast.makeText(this, "The app was not allowed to write in your storage", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * Ensures the write permission and shares the image
     */
    public void ensurePermissionAndShareImage() {

        Timber.d("Sharing image");

        if(Build.VERSION.SDK_INT >= 23) {

            Timber.d("SDK is greater than 23");

            if(!hasPermissions(this, PERMISSIONS)) {

                Timber.d("App does not have permission yet... Asking");

                ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST);
            } else {

                saveAndShareImage();
            }
        } else {

            Timber.d("SDK is less than 23");

            saveAndShareImage();
        }
    }

    /**
     * Checks if the specified permissions are granted
     *
     * @param context     The context
     * @param permissions The permissions to check
     * @return A flag describing the outcome of the check
     */
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

    /**
     * Saves and shares the image
     */
    private void saveAndShareImage() {

        Timber.d("Starting to save and share the image...");

        // Generate a unique name for the image
        String imageName = StorageUtils.getRandomFileName("jpeg");

        // Declare an empty image URI
        Uri imageUri = null;

        try {

            // Insert the image into the media store and retrieve the URI
            imageUri = ImagesUtils.insertImage(getContentResolver(), mBitmap, imageName, "Made with Typo App");
        } catch (Exception e) {

            Timber.e(e, "Error while saving the image");

            Toast.makeText(this, "Something went wrong while saving the image :(", Toast.LENGTH_SHORT).show();
        }

        Timber.d("Utility class image URI: " + imageUri);

        // Create the share intent
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("image/*")
                .setStream(imageUri)
                .setChooserTitle("Share with")
                .getIntent();

        // Add the read permission on URI flag
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(shareIntent, "Share"));
    }

    /**
     * Saves a bitmap to file
     *
     * @param bitmap the bitmap to save
     * @param name   the file name
     * @return the uri of the saved image
     */
    private Uri saveBitmapToFile(Bitmap bitmap, String name) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, name, null);

        return Uri.parse(path);
    }

    /**
     * Applies the selected theme to the edit text view
     */
    private void applySelectedTheme() {

        Timber.d("Applying theme " + mTheme.name + "...");

        // Get specified typeface from the assets
        Typeface typeface = Typeface.createFromAsset(
                getApplicationContext().getAssets(),
                String.format(Locale.US, "fonts/%s", mTheme.font));

        // Set the typeface
        mEditText.setTypeface(typeface);

        // Set text size
        mEditText.setTextSize(BitmapUtils.convertDpToPx(this, mTheme.fontSize));

        // Set text and background colors
        mEditText.setTextColor(mTheme.getFontColor());
        mEditText.setBackgroundColor(mTheme.getBackgroundColor());

        // Get padding
        int padding = BitmapUtils.convertDpToPx(this, 32);

        // Set padding
        mEditText.setPadding(padding, padding, padding, padding);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            // Set letter spacing
            mEditText.setLetterSpacing(mTheme.letterSpacing);
        }

        // Set line spacing
        mEditText.setLineSpacing(mTheme.lineSpacing, 1);

        // Get text gravity
        int gravity = ThemesUtils.getHorizontalGravity(this.mTheme.horizontalAlignment) |
                ThemesUtils.getVerticalGravity(this.mTheme.verticalAlignment);

        // Set text gravity
        mEditText.setGravity(gravity);

        mTextSettingsFragment.setLetterSpacing(mTheme.letterSpacing);
        mTextSettingsFragment.setLineSpacing(mTheme.lineSpacing);
        mTextSettingsFragment.setTextSize(mTheme.fontSize);
    }

    /**
     * Called when the theme is selected
     *
     * @param theme The selected theme
     */
    @Override
    public void onThemeSelected(Theme theme) {

        // Keep track of the selected theme
        mTheme = theme;

        // Apply the changes to the edit text
        applySelectedTheme();
    }

    /**
     * Called when the text size is changed
     *
     * @param size The size
     */
    @Override
    public void onTextSizeChanged(int size) {

        // Set the theme with the new size
        mTheme.fontSize = size;

        // Apply the changes to the edit text
        applySelectedTheme();
    }

    /**
     * Called when letter spacing is changed
     *
     * @param spacing The letter spacing
     */
    @Override
    public void onLetterSpacingChanged(float spacing) {

        // Set the letter spacing to the theme
        mTheme.letterSpacing = spacing;

        // Apply the changes to the edit text
        applySelectedTheme();
    }

    /**
     * Called when line spacing is changed
     *
     * @param spacing The line spacing
     */
    @Override
    public void onLineSpacingChanged(float spacing) {

        // Set the line spacing to the theme
        mTheme.lineSpacing = spacing;

        // Apply the changes to the edit text
        applySelectedTheme();
    }

    @Override
    public void onEditStarted() {

    }

    @Override
    public void onEditCompleted() {

    }

    /**
     * Called when horizontal text alignment is changed
     *
     * @param alignment The horizontal text alignment
     */
    @Override
    public void onTextAlignmentChanged(String alignment) {

        Timber.d("Horizontal aligment changed to: " + alignment);

        // Set the alignment to the theme
        mTheme.horizontalAlignment = alignment;

        // Apply the changes to the edit text
        applySelectedTheme();
    }

    /**
     * Called when the vertical text alignment is changed
     *
     * @param alignment The vertical text alignment
     */
    @Override
    public void onTextVerticalAlignmentChanged(String alignment) {
        Timber.d("Vertical alignment changed to: " + alignment);

        // Set the alignment to the theme
        mTheme.verticalAlignment = alignment;

        // Apply the changes to the edit text
        applySelectedTheme();
    }
}
