package com.github.p1va.typoapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.github.p1va.typoapp.R;

import timber.log.Timber;

/**
 * The Text Settings fragment
 */
public class TextSettingsFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    /**
     * The seek bar default progress value
     */
    private static final int SEEK_BAR_PROGRESS_STEP = 1;

    /**
     * The seek bar for text size minimum progress
     */
    private static final int SEEK_BAR_TEXT_SIZE_MIN_PROGRESS = 3;

    /**
     * The seek bar for text size maximum progress
     */
    private static final int SEEK_BAR_TEXT_SIZE_MAX_PROGRESS = 20;

    /**
     * The seek bar for text size maximum allowed progress
     */
    private static final int SEEK_BAR_TEXT_SIZE_MAX_ALLOWED_PROGRESS = (SEEK_BAR_TEXT_SIZE_MAX_PROGRESS - SEEK_BAR_TEXT_SIZE_MIN_PROGRESS) / SEEK_BAR_PROGRESS_STEP;

    /**
     * The seek bar for letter spacing progress multiplier
     */
    private static final float SEEK_BAR_LETTER_SPACING_PROGRESS_MULTIPLIER = 0.01f;

    /**
     * The seek bar for letter spacing minimum progress
     */
    private static final int SEEK_BAR_LETTER_SPACING_MIN_PROGRESS = -10;

    /**
     * The seek bar for letter spacing maximum progress
     */
    private static final int SEEK_BAR_LETTER_SPACING_MAX_PROGRESS = 10;

    /**
     * The seek bar for letter spacing maximum allowed progress
     */
    private static final int SEEK_BAR_LETTER_SPACING_MAX_ALLOWED_PROGRESS = (SEEK_BAR_LETTER_SPACING_MAX_PROGRESS - SEEK_BAR_LETTER_SPACING_MIN_PROGRESS) / SEEK_BAR_PROGRESS_STEP;

    /**
     * The seek bar for line spacing minimum progress
     */
    private static final int SEEK_BAR_LINE_SPACING_MIN_PROGRESS = -20;

    /**
     * The seek bar for line spacing maximum progress
     */
    private static final int SEEK_BAR_LINE_SPACING_MAX_PROGRESS = 40;

    /**
     * The seek bar for line spacing maximum allowed progress
     */
    private static final int SEEK_BAR_LINE_SPACING_MAX_ALLOWED_PROGRESS = (SEEK_BAR_LINE_SPACING_MAX_PROGRESS - SEEK_BAR_LINE_SPACING_MIN_PROGRESS) / SEEK_BAR_PROGRESS_STEP;

    /**
     * The interaction listener
     */
    private OnFragmentInteractionListener mListener;

    /**
     * The seek bar for letter spacing
     */
    private SeekBar mSeekBarLetterSpacing;

    /**
     * The seek bar for text size
     */
    private SeekBar mSeekBarTextSize;

    /**
     * The seek bar for line spacing
     */
    private SeekBar mSeekBarLineSpacing;

    /**
     * Called when fragment is attached
     *
     * @param context The context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof TextSettingsFragment.OnFragmentInteractionListener) {

            // Cast context to listener to notify on changes
            mListener = (TextSettingsFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Called on detachment
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /***
     * Gets a new instance of the TextSettingsFragment
     * @return
     */
    public static TextSettingsFragment newInstance(int textSize, float lineSpacing, float letterSpacing) {

        Bundle args = new Bundle();
        args.putSerializable("text_size", textSize);
        args.putSerializable("line_spacing", lineSpacing);
        args.putSerializable("letter_spacing", letterSpacing);

        TextSettingsFragment fragment = new TextSettingsFragment();

        fragment.setArguments(args);

        return fragment;
    }

    /**
     * Called on fragment creation
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called on view creation
     *
     * @param inflater           The inflater
     * @param container          The container
     * @param savedInstanceState The saved instance state
     * @return The view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Retrieve initial values from args
        int textSize = (int) getArguments().getSerializable("text_size");
        float lineSpacing = (float) getArguments().getSerializable("line_spacing");
        float letterSpacing = (float) getArguments().getSerializable("letter_spacing");

        View view = inflater.inflate(R.layout.fragment_text_settings, container, false);

        // Find views
        mSeekBarLetterSpacing = view.findViewById(R.id.seekbar_letter_spacing);
        mSeekBarTextSize = view.findViewById(R.id.seekbar_text_size);
        mSeekBarLineSpacing = view.findViewById(R.id.seekbar_line_spacing);

        // Set seek bar max
        mSeekBarLetterSpacing.setMax(SEEK_BAR_LETTER_SPACING_MAX_ALLOWED_PROGRESS);
        mSeekBarTextSize.setMax(SEEK_BAR_TEXT_SIZE_MAX_ALLOWED_PROGRESS);
        mSeekBarLineSpacing.setMax(SEEK_BAR_LINE_SPACING_MAX_ALLOWED_PROGRESS);

        setTextSize(textSize);
        setLetterSpacing(letterSpacing);
        setLineSpacing(lineSpacing);

        // Set listeners
        mSeekBarLetterSpacing.setOnSeekBarChangeListener(this);
        mSeekBarTextSize.setOnSeekBarChangeListener(this);
        mSeekBarLineSpacing.setOnSeekBarChangeListener(this);

        return view;
    }

    /**
     * Called when seek bars progress changes
     *
     * @param seekBar  The seek bar
     * @param progress The progress
     * @param fromUser The from user flag
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        switch (seekBar.getId()) {

            case R.id.seekbar_letter_spacing:

                float letterSpacingValue = SEEK_BAR_LETTER_SPACING_MIN_PROGRESS + (progress * SEEK_BAR_PROGRESS_STEP);

                letterSpacingValue = letterSpacingValue * SEEK_BAR_LETTER_SPACING_PROGRESS_MULTIPLIER;

                if(mListener != null) {
                    mListener.onLetterSpacingChanged(letterSpacingValue);
                }

                break;

            case R.id.seekbar_text_size:

                float textSizeValue = SEEK_BAR_TEXT_SIZE_MIN_PROGRESS + (progress * SEEK_BAR_PROGRESS_STEP);

                if(mListener != null) {
                    mListener.onTextSizeChanged((int) textSizeValue);
                }

                break;

            case R.id.seekbar_line_spacing:

                float lineSpacingValue = SEEK_BAR_LINE_SPACING_MIN_PROGRESS + (progress * SEEK_BAR_PROGRESS_STEP);

                if(mListener != null) {
                    mListener.onLineSpacingChanged(lineSpacingValue);
                }

                break;
        }
    }

    /**
     * Called on start tracking touch
     *
     * @param seekBar The seek bar
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if(mListener != null)
            mListener.onEditStarted();
    }

    /**
     * Called on stop tracking touch
     *
     * @param seekBar The seek bar
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(mListener != null)
            mListener.onEditCompleted();
    }

    /**
     * Sets the text size seek bar
     *
     * @param textSize The text size
     */
    public void setTextSize(int textSize) {
        Timber.d("Setting text size seek bar progress to " + textSize);

        if(mSeekBarTextSize != null) {

            // Cast progress to int
            int progress = textSize;

            if(progress < SEEK_BAR_TEXT_SIZE_MIN_PROGRESS || progress > SEEK_BAR_TEXT_SIZE_MAX_PROGRESS) {

                Timber.w(String.format("You are trying to set progress for text size to %d. It has to be a value between 0 and %d", textSize, SEEK_BAR_TEXT_SIZE_MAX_PROGRESS));
            } else {

                progress = (textSize - SEEK_BAR_TEXT_SIZE_MIN_PROGRESS) / SEEK_BAR_PROGRESS_STEP;

                // Update the seek bar progress
                mSeekBarTextSize.setProgress(progress);
            }
        } else {
            Timber.w("Text size seek bar is null...");
        }
    }

    /**
     * Sets the line spacing seek bar
     *
     * @param lineSpacing The line spacing
     */
    public void setLineSpacing(float lineSpacing) {
        Timber.d("Setting line spacing seek bar progress to " + lineSpacing);

        if(mSeekBarLineSpacing != null) {

            // Cast progress to int
            int progress = (int) lineSpacing;

            if(progress < SEEK_BAR_LINE_SPACING_MIN_PROGRESS || progress > SEEK_BAR_LINE_SPACING_MAX_PROGRESS) {

                Timber.w(String.format("You are trying to set progress for line spacing to %d. It has to be a value between 0 and %d", progress, SEEK_BAR_LINE_SPACING_MAX_PROGRESS));
            } else {

                progress = (progress - SEEK_BAR_LINE_SPACING_MIN_PROGRESS) / SEEK_BAR_PROGRESS_STEP;

                // Update the seek bar progress
                mSeekBarLineSpacing.setProgress(progress);
            }
        } else {
            Timber.w("Line spacing seek bar is null...");
        }
    }

    /**
     * Sets the letter spacing seek bar
     *
     * @param letterSpacing The letter spacing
     */
    public void setLetterSpacing(float letterSpacing) {

        Timber.d("Setting letter spacing seek bar progress to " + letterSpacing);

        if(mSeekBarLetterSpacing != null) {

            // Cast progress to int
            int progress = (int) (letterSpacing / SEEK_BAR_LETTER_SPACING_PROGRESS_MULTIPLIER);

            if(progress < SEEK_BAR_LETTER_SPACING_MIN_PROGRESS || progress > SEEK_BAR_LETTER_SPACING_MAX_PROGRESS) {

                Timber.w(String.format("You are trying to set progress for letter spacing to %d. It has to be a value between 0 and %d", progress, SEEK_BAR_LETTER_SPACING_MAX_PROGRESS));
            } else {

                progress = (progress - SEEK_BAR_LETTER_SPACING_MIN_PROGRESS) / SEEK_BAR_PROGRESS_STEP;

                // Update the seek bar progress
                mSeekBarLetterSpacing.setProgress(progress);
            }
        } else {
            Timber.w("Letter spacing seek bar is null...");
        }
    }

    /**
     * The fragment interaction listener interface
     */
    public interface OnFragmentInteractionListener {

        /**
         * Called on text size changes
         *
         * @param size The size
         */
        void onTextSizeChanged(int size);

        /**
         * Called on letter spacing changes
         *
         * @param spacing The letter spacing
         */
        void onLetterSpacingChanged(float spacing);

        /**
         * Called on line spacing changes
         *
         * @param spacing The line spacing
         */
        void onLineSpacingChanged(float spacing);

        /**
         * Called on edit started
         */
        void onEditStarted();

        /**
         * Called on edit completed
         */
        void onEditCompleted();
    }
}
