package com.ruxbit.bikecompanion.ads;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.ruxbit.bikecompanion.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AdFragment extends Fragment {
    private static final String ADMOB_APP_ID = "ca-app-pub-3940256099942544~3347511713";

    @BindView(R.id.adView) AdView adView;

    public AdFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ad, container, false);
        ButterKnife.bind(this, view);

        MobileAds.initialize(getContext(), ADMOB_APP_ID);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);

        return view;
    }

}
