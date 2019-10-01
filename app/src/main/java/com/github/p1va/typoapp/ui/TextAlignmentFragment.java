package com.github.p1va.typoapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.github.p1va.typoapp.R;

import timber.log.Timber;

public class TextAlignmentFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    public TextAlignmentFragment() {
    }

    /***
     * Gets a new instance of the TextAlignmentFragment
     * @return
     */
    public static TextAlignmentFragment newInstance() {
        TextAlignmentFragment fragment = new TextAlignmentFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            // Retrieve args
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_text_justification, container, false);

        // Horizontal alignment buttons
        ImageButton horizontalLeftAlignedButton = view.findViewById(R.id.image_button_align_left);
        ImageButton horizontalCenterAlignedButton = view.findViewById(R.id.image_button_align_center);
        ImageButton horizontalRightAlignedButton = view.findViewById(R.id.image_button_align_right);

        // Vertical alignment buttons
        ImageButton verticalBottomAlignedButton = view.findViewById(R.id.image_button_align_vertical_bottom);
        ImageButton verticalCenterAlignedButton = view.findViewById(R.id.image_button_align_vertical_center);
        ImageButton verticalTopAlignedButton = view.findViewById(R.id.image_button_align_vertical_top);

        // Set listeners
        horizontalLeftAlignedButton.setOnClickListener(this);
        horizontalCenterAlignedButton.setOnClickListener(this);
        horizontalRightAlignedButton.setOnClickListener(this);

        // Set listeners
        verticalBottomAlignedButton.setOnClickListener(this);
        verticalCenterAlignedButton.setOnClickListener(this);
        verticalTopAlignedButton.setOnClickListener(this);

        return view;
    }

    public static String getId(View view) {
        if(view.getId() == 0xffffffff) return "no-id";
        else return view.getResources().getResourceName(view.getId());
    }

    @Override
    public void onClick(View v) {

        Timber.d("On click view:" + getId(v));

        String horizontalAlignment = "";
        String verticalAlignment = "";

        switch (v.getId()) {

            // Handle horizontal alignment
            case R.id.image_button_align_left:
                horizontalAlignment = "left";
                break;

            case R.id.image_button_align_center:
                horizontalAlignment = "center";
                break;

            case R.id.image_button_align_right:
                horizontalAlignment = "right";
                break;

            // Handle vertical alignment
            case R.id.image_button_align_vertical_bottom:
                verticalAlignment = "bottom";
                break;

            case R.id.image_button_align_vertical_center:
                verticalAlignment = "center";
                break;

            case R.id.image_button_align_vertical_top:
                verticalAlignment = "top";
                break;

            default:
                return;
        }

        Timber.d("HA:" + horizontalAlignment + "  VA:" + verticalAlignment);

        if(mListener != null && !horizontalAlignment.isEmpty())
            mListener.onTextAlignmentChanged(horizontalAlignment);

        if(mListener != null && !verticalAlignment.isEmpty())
            mListener.onTextVerticalAlignmentChanged(verticalAlignment);
    }

    /**
     * The fragment interaction listener interface
     */
    public interface OnFragmentInteractionListener {

        /**
         * Called when horizontal text alignment is changed
         * @param alignment The new alignment
         */
        void onTextAlignmentChanged(String alignment);

        /**
         * Called when vertical text alignment is changed
         * @param alignment The new alignment
         */
        void onTextVerticalAlignmentChanged(String alignment);
    }
}
