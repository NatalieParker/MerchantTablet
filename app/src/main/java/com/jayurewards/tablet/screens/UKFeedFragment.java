package com.jayurewards.tablet.screens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jayurewards.tablet.GlideApp;
import com.jayurewards.tablet.R;

public class UKFeedFragment extends Fragment {

    private static final String URL = "url";
    private String url;

    public UKFeedFragment() {
        // Required empty public constructor
    }

    public static UKFeedFragment newInstance(String url) {
        UKFeedFragment fragment = new UKFeedFragment();
        Bundle args = new Bundle();
        args.putString(URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString(URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uk_feed, container, false);
        ImageView design = view.findViewById(R.id.imageUKFeedImage);

        if (getActivity() != null) {
            GlideApp.with(getActivity())
                    .load(url)
                    .into(design);
        }

        return view;
    }
}