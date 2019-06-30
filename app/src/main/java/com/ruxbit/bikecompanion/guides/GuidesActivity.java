package com.ruxbit.bikecompanion.guides;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.ruxbit.bikecompanion.R;
import com.ruxbit.bikecompanion.model.Guide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GuidesActivity extends AppCompatActivity implements GuidesAdapter.GuidesAdapterOnClickHandler {
    @BindView(R.id.rv_ag_guides) RecyclerView rvGuides;

    private GuidesAdapter guidesAdapter;
    private GuidesViewModel guidesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guides);
        ButterKnife.bind(this);

        guidesAdapter = new GuidesAdapter(this, this);
        rvGuides.setAdapter(guidesAdapter);

        guidesViewModel = ViewModelProviders.of(this).get(GuidesViewModel.class);
        guidesViewModel.getGuides().observe(this, new Observer<List<Guide>>() {
            @Override
            public void onChanged(List<Guide> guides) {
                guidesAdapter.setGuidesData(guides);
            }
        });
    }

    @Override
    public void onClick(Guide guide) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(guide.getVideoUri());
        startActivity(intent);
    }
}
