package com.ruxbit.bikecompanion.bike.parts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ruxbit.bikecompanion.utils.Constants;
import com.ruxbit.bikecompanion.R;
import com.ruxbit.bikecompanion.model.Part;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddPartDialogFragment extends DialogFragment {
    @BindView(R.id.spinner_part_type) Spinner spinnerPartType;
    @BindView(R.id.et_part_manufacturer) EditText etPartManufacturer;
    @BindView(R.id.et_part_model) EditText etPartModel;

    private Part part;
    private String bikeId;

    public AddPartDialogFragment() {
    }

    public interface AddPartDialogListener {
        void onAddPart(Part part);
        void onUpdatePart(Part part);
    }


    public static AddPartDialogFragment newInstance(String bikeId) {
        AddPartDialogFragment f = new AddPartDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TAG_BIKE_ID, bikeId);
        f.setArguments(bundle);
        return f;
    }

    public static AddPartDialogFragment newInstance(Part part) {
        AddPartDialogFragment f = new AddPartDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.TAG_PART, Parcels.wrap(part));
        f.setArguments(bundle);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_add_part, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.part_type_names, R.layout.support_simple_spinner_dropdown_item);
        spinnerPartType.setAdapter(adapter);

        if (getArguments().containsKey(Constants.TAG_BIKE_ID)) {
            bikeId = getArguments().getString(Constants.TAG_BIKE_ID);
        } else {
            part = Parcels.unwrap(getArguments().getParcelable(Constants.TAG_PART));
            bikeId = part.getBikeId();
            etPartManufacturer.setText(part.getManufacturer());
            etPartModel.setText(part.getModel());
            spinnerPartType.setSelection(part.getType());
        }
    }

    @OnClick(R.id.btn_save_part)
    public void onClickSavePart() {
        int type = (int)spinnerPartType.getSelectedItemId();
        String manufacturer = etPartManufacturer.getText().toString();
        String model = etPartModel.getText().toString();


        AddPartDialogListener listener = (AddPartDialogListener) getTargetFragment();
        if (part != null) {
            part.setType(type);
            part.setManufacturer(manufacturer);
            part.setModel(model);
            listener.onUpdatePart(part);
        } else {
            part = new Part(bikeId, type, manufacturer, model);
            listener.onAddPart(part);
        }
        dismiss();
    }

    @OnClick(R.id.btn_cancel_part)
    public void onClickCancelPart() {
        dismiss();
    }
}
