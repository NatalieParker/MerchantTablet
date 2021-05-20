package com.jayurewards.tablet;

import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jayurewards.tablet.models.OffersModel;
import com.jayurewards.tablet.screens.RA_UserKeypad;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewPagerCard2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewPagerCard2Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<OffersModel> offersFragment = new ArrayList<>();
    private RecyclerView rv;
    private NestedScrollView scrollView;

    public ViewPagerCard2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewPagerCard2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewPagerCard2Fragment newInstance(String param1, String param2) {
        ViewPagerCard2Fragment fragment = new ViewPagerCard2Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_pager_card2, container, false);
        rv = view.findViewById(R.id.recyclerViewUserKeypadViewPagerCard2Rewards);
        scrollView = view.findViewById(R.id.scrollViewUserKeypadViewPagerCard2);

        startRecyclerView(offersFragment);

        return view;
    }

    private void startRecyclerView(ArrayList<OffersModel> offersList) {

        Log.i("TAG", "\n\n ACTIVITY IS NULL " + getActivity());

        RA_UserKeypad adapter = new RA_UserKeypad(offersList, getActivity());
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(lm);
        rv.setAdapter(adapter);
    }
}