package com.ruxbit.bikecompanion.onboarding;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.leanback.app.OnboardingSupportFragment;

import com.ruxbit.bikecompanion.utils.Constants;
import com.ruxbit.bikecompanion.R;


import static android.content.Context.MODE_PRIVATE;

public class AppOnboardingFragment extends OnboardingSupportFragment {
    private static final long ANIMATION_DURATION = 1000;
    private ImageView contentView;

    private String[] titles;
    private String[] descriptions;
    TypedArray backgrounds;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        titles = getResources().getStringArray(R.array.onboarding_titles);
        descriptions = getResources().getStringArray(R.array.onboarding_descriptions);
        backgrounds = getResources().obtainTypedArray(R.array.onboarding_images);
    }

    @Override
    protected int getPageCount() {
        return 3;
    }

    @Override
    protected CharSequence getPageTitle(int pageIndex) {
        return titles[pageIndex];
    }

    @Override
    protected CharSequence getPageDescription(int pageIndex) {
        return descriptions[pageIndex];
    }

    @Override
    protected void onPageChanged(int newPage, int previousPage) {
        super.onPageChanged(newPage, previousPage);
        contentView.setScaleX(0.6f);
        contentView.setScaleY(0.6f);
        contentView.setImageResource(backgrounds.getResourceId(newPage, 0));
    }

    @Nullable
    @Override
    protected View onCreateBackgroundView(LayoutInflater inflater, ViewGroup container) {
        return null;
    }

    @Nullable
    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container) {
        contentView = new ImageView(getContext());
        contentView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        contentView.setImageResource(backgrounds.getResourceId(0, 0));
        contentView.setPadding(0, 32, 0, 32);
        return contentView;
    }

    @Nullable
    @Override
    protected View onCreateForegroundView(LayoutInflater inflater, ViewGroup container) {
        return null;
    }

    @Override
    protected void onFinishFragment() {
        SharedPreferences prefs = getActivity().getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        prefs.edit().putInt(Constants.PREF_TAG_STATE, Constants.PREF_STATE_NO_CODE).apply();
        getActivity().finish();
    }
}
