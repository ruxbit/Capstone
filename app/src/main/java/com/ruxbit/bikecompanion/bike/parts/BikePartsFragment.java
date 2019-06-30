package com.ruxbit.bikecompanion.bike.parts;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ruxbit.bikecompanion.utils.Constants;
import com.ruxbit.bikecompanion.R;
import com.ruxbit.bikecompanion.bike.BikeActivity;
import com.ruxbit.bikecompanion.bike.BikeViewModel;
import com.ruxbit.bikecompanion.model.Part;
import com.ruxbit.bikecompanion.model.Task;
import com.ruxbit.bikecompanion.tasks.TasksActivity;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BikePartsFragment extends Fragment implements PartsAdapter.PartsAdapterOnClickHandler, AddPartDialogFragment.AddPartDialogListener, BikeActivity.AddPartClickHandler {
    @BindView(R.id.rv_parts) RecyclerView rvParts;

    private BikeViewModel bikeViewModel;
    private PartsAdapter partsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bike_parts, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        rvParts.setLayoutManager(lm);
        rvParts.setHasFixedSize(true);
        partsAdapter = new PartsAdapter(this, this.getResources());
        rvParts.setAdapter(partsAdapter);

        bikeViewModel = ViewModelProviders.of(getActivity()).get(BikeViewModel.class);
        bikeViewModel.getParts().observe(this, new Observer<List<Part>>() {
            @Override
            public void onChanged(List<Part> parts) {
                partsAdapter.setPartsData(parts);
            }
        });
        bikeViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                partsAdapter.setTasksData(tasks);
            }
        });
    }

    @Override
    public void onClick(Part part) {
        Intent intent = new Intent(getActivity(), TasksActivity.class);
        intent.putExtra(Constants.INTENT_PART, Parcels.wrap(part));
        startActivity(intent);
    }

    @Override
    public void onDelete(Part part) {
        bikeViewModel.deletePart(part);
    }

    @Override
    public void onEdit(Part part) {
        FragmentManager fragmentManager = getFragmentManager();
        AddPartDialogFragment f = AddPartDialogFragment.newInstance(part);
        f.setTargetFragment(this, 300);
        f.show(fragmentManager, "dialog_fragment_add_part");
    }

    @Override
    public void onClickAddPart() {
        FragmentManager fragmentManager = getFragmentManager();
        AddPartDialogFragment f = AddPartDialogFragment.newInstance(bikeViewModel.getBikeId());
        f.setTargetFragment(this, 300);
        f.show(fragmentManager, "dialog_fragment_add_part");
    }

    @Override
    public void onAddPart(Part part) {
        bikeViewModel.addPart(part);
    }

    public void onUpdatePart(Part part) {bikeViewModel.updatePart(part);}
}
