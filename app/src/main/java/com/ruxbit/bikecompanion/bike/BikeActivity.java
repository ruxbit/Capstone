package com.ruxbit.bikecompanion.bike;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.palette.graphics.Palette;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.ruxbit.bikecompanion.utils.Constants;
import com.ruxbit.bikecompanion.R;
import com.ruxbit.bikecompanion.bike.details.BikeDetailsFragment;
import com.ruxbit.bikecompanion.bike.parts.BikePartsFragment;
import com.ruxbit.bikecompanion.bike.rides.BikeRidesFragment;
import com.ruxbit.bikecompanion.model.Bike;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BikeActivity extends AppCompatActivity {
    @BindView(R.id.vp_bike) ViewPager vpBike;
    @BindView(R.id.tl_bike) TabLayout tlBike;
    @BindView(R.id.iv_ab_photo) ImageView ivAbPhoto;
    @BindView(R.id.fab_add_part) FloatingActionButton fabAddPart;
    @BindView(R.id.toolbar_ab) Toolbar toolbar;

    private BikeViewModel bikeViewModel;
    private AddPartClickHandler addPartClickHandler;

    public interface AddPartClickHandler {
        void onClickAddPart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_bike);
        ButterKnife.bind(this);

        String bikeId = getIntent().getExtras().getString(Constants.INTENT_BIKE_ID);
        bikeViewModel = ViewModelProviders.of(this).get(BikeViewModel.class);
        bikeViewModel.initBikeId(bikeId);

        bikeViewModel.getBike().observe(this, new Observer<Bike>() {
            @Override
            public void onChanged(Bike bike) {
                if (bike.getImage() != null) {
                    ivAbPhoto.setImageBitmap(bike.getImage());
                    Palette.from(bike.getImage()).generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(@Nullable Palette palette) {
                            setToolbarColor(palette);
                        }
                    });
                }
            }
        });
        setupViewPager();
    }

    private void setupViewPager() {
        BikePageAdapter adapter = new BikePageAdapter(getSupportFragmentManager());
        adapter.addFragment(new BikeDetailsFragment(), getResources().getString(R.string.details));
        BikePartsFragment fragment = new BikePartsFragment();
        addPartClickHandler = (AddPartClickHandler) fragment;
        adapter.addFragment(fragment, getResources().getString(R.string.parts));
        adapter.addFragment(new BikeRidesFragment(), getResources().getString(R.string.rides));
        vpBike.setAdapter(adapter);
        tlBike.setupWithViewPager(vpBike);


        final Activity activity = this;
        vpBike.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                hideKeyboard(activity);
                if (position == 1)
                    fabAddPart.show();
                else
                    fabAddPart.hide();
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Sets toolbar color based on Palette
     * @param p
     */
    public void setToolbarColor(Palette p) {
        Palette.Swatch swatch = p.getMutedSwatch();

        int backgroundColor = ContextCompat.getColor(this, R.color.colorPrimary);
        //int textColor = ContextCompat.getColor(this, R.color.colorTextSecondary);

        // Check that the Vibrant swatch is available
        if(swatch != null){
            backgroundColor = swatch.getRgb();
            //textColor = vibrantSwatch.getTitleTextColor();
        }

        // Set the toolbar background and text colors
        tlBike.setBackgroundColor(backgroundColor);
        //tlBike.setTitleTextColor(textColor);
    }

    @OnClick(R.id.fab_add_part)
    public void onClickAddPart() {
        addPartClickHandler.onClickAddPart();
    }
}
