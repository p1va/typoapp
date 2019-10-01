package com.github.p1va.typoapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.p1va.typoapp.R;
import com.github.p1va.typoapp.models.Theme;
import com.github.p1va.typoapp.adapters.ThemesAdapter;

import java.util.ArrayList;

import timber.log.Timber;

public class ThemesFragment extends Fragment implements ThemesAdapter.ItemClickListener {

    private static final String THEMES_LIST_KEY = "themes";
    private ArrayList<Theme> mThemes;

    public static ThemesFragment newInstance(ArrayList<Theme> themes) {

        // Create new fragment
        ThemesFragment fragment = new ThemesFragment();

        // Create new bundle
        Bundle bundle = new Bundle();

        // Pass themes array
        bundle.putSerializable(THEMES_LIST_KEY, themes);

        // Set the arguments
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Get view
        View view = inflater.inflate(R.layout.fragment_themes, container, false);

        // Retrieve themes
        mThemes = (ArrayList<Theme>) getArguments().getSerializable(THEMES_LIST_KEY);

        // Declare adapter for themes list
        ThemesAdapter mThemesAdapter = new ThemesAdapter(this.getContext(), mThemes);
        mThemesAdapter.setClickListener(this);

        // Set up the RecyclerView
        RecyclerView mRecyclerView = view.findViewById(R.id.themes_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(mThemesAdapter);

        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        Timber.d("Theme at position " + position + " has been clicked.");

        // Retrieve theme
        Theme theme = mThemes.get(position);

        if(mListener != null)
            mListener.onThemeSelected(theme);
    }

    OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onThemeSelected(Theme theme);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof ThemesFragment.OnFragmentInteractionListener) {
            mListener = (ThemesFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
