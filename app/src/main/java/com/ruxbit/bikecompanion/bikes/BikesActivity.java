package com.ruxbit.bikecompanion.bikes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ruxbit.bikecompanion.guides.GuidesActivity;
import com.ruxbit.bikecompanion.login.LoginActivity;
import com.ruxbit.bikecompanion.onboarding.OnboardingActivity;
import com.ruxbit.bikecompanion.model.Bike;
import com.ruxbit.bikecompanion.utils.Constants;
import com.ruxbit.bikecompanion.R;
import com.ruxbit.bikecompanion.bike.BikeActivity;
import com.ruxbit.bikecompanion.utils.LoadingState;
import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Field;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BikesActivity extends AppCompatActivity implements BikesAdapter.BikesAdapterOnClickHandler {
    @BindView(R.id.rv_ab_bikes) RecyclerView rvBikes;
    @BindView(R.id.srl_ab_refresh) SwipeRefreshLayout srlRefresh;
    @BindView(R.id.pb_ab_loading) ProgressBar pbLoading;
    @BindView(R.id.cl_ab_container) ConstraintLayout clContainer;
    @BindView(R.id.toolbar_ab) Toolbar toolbar;

    private BikesAdapter bikesAdapter;
    private BikesViewModel bikesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bikes);
        ButterKnife.bind(this);
        disableSrlLoadingIndicator();
        setSupportActionBar(toolbar);

        bikesAdapter = new BikesAdapter(this, getResources());
        rvBikes.setAdapter(bikesAdapter);
        srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bikesViewModel.updateBikes();
                srlRefresh.setRefreshing(false);
            }
        });

        bikesViewModel = ViewModelProviders.of(this).get(BikesViewModel.class);
        bikesViewModel.getBikes().observe(this, new Observer<List<Bike>>() {
            @Override
            public void onChanged(List<Bike> bikes) {
                bikesAdapter.setBikesData(bikes);
            }
        });

        bikesViewModel.getLoadingState().observe(this, new Observer<LoadingState>() {
            @Override
            public void onChanged(LoadingState loadingState) {
                switch (loadingState.getState()) {
                    case LoadingState.LOADING:
                        pbLoading.setVisibility(View.VISIBLE);
                        break;
                    case LoadingState.INACTIVE: case LoadingState.SUCCESS:
                        pbLoading.setVisibility(View.GONE);
                        break;
                    default:
                        pbLoading.setVisibility(View.GONE);
                        TypedArray typedArray = getResources().obtainTypedArray(R.array.errors);
                        String errorText = typedArray.getString(loadingState.getState());
                        typedArray.recycle();
                        Snackbar.make(clContainer, errorText, Snackbar.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        int state = prefs.getInt(Constants.PREF_TAG_STATE, Constants.PREF_STATE_FIRST_LAUNCH);
        switch (state) {
            //App is first launched
            case Constants.PREF_STATE_FIRST_LAUNCH:
                Intent intent = new Intent(this, OnboardingActivity.class);
                startActivity(intent);
                break;
            //App was laucnhed previously but the user never logged in
            case Constants.PREF_STATE_NO_CODE:
                if (Intent.ACTION_VIEW.equals(getIntent().getAction())) {
                    //Process RedirectUri from Strava
                    Uri uri = getIntent().getData();
                    bikesViewModel.setAuthorizationCodeFromUri(this, uri);
                } else {
                    //Start login activity
                    Intent loginIntent = new Intent(this, LoginActivity.class);
                    startActivity(loginIntent);
                }
                break;
            case Constants.PREF_STATE_HAS_CODE:
                break;
        }
    }

    @Override
    public void onClick(Bike bike) {
        Intent intent = new Intent(this, BikeActivity.class);
        intent.putExtra(Constants.INTENT_BIKE_ID, bike.getId());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_guides:
                Intent intent = new Intent(this, GuidesActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Disables default SwipeRefreshLayout update animation
     */
    private void disableSrlLoadingIndicator() {
        try {
            Field f = srlRefresh.getClass().getDeclaredField("mCircleView");
            f.setAccessible(true);
            ImageView img = (ImageView)f.get(srlRefresh);
            img.setAlpha(0.0f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
