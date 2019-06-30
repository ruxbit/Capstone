package com.ruxbit.bikecompanion.bike.rides;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.ruxbit.bikecompanion.R;
import com.ruxbit.bikecompanion.bike.BikeViewModel;
import com.ruxbit.bikecompanion.model.Ride;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BikeRidesFragment extends Fragment implements RidesAdapter.RidesAdapterOnClickHandler {
    @BindView(R.id.rv_fbr_rides) RecyclerView rvRides;
    private RidesAdapter ridesAdapter;
    private BikeViewModel bikeViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bike_rides, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ridesAdapter = new RidesAdapter(this, this.getResources());
        rvRides.setAdapter(ridesAdapter);
        rvRides.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rvRides.setHasFixedSize(true);

        bikeViewModel = ViewModelProviders.of(getActivity()).get(BikeViewModel.class);
        bikeViewModel.getRides().observe(this, new Observer<List<Ride>>() {
            @Override
            public void onChanged(List<Ride> rides) {
                ridesAdapter.setRidesData(rides);
            }
        });
    }

    @Override
    public void onClick(Ride part) {

    }
}
