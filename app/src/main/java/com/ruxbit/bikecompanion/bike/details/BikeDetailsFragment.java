package com.ruxbit.bikecompanion.bike.details;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ruxbit.bikecompanion.utils.Constants;
import com.ruxbit.bikecompanion.R;
import com.ruxbit.bikecompanion.bike.BikeViewModel;
import com.ruxbit.bikecompanion.model.Bike;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class BikeDetailsFragment extends Fragment {
    @BindView(R.id.iv_fbd_photo) ImageView ivPhoto;
    @BindView(R.id.et_fbd_name) TextView etName;
    @BindView(R.id.spinner_fbd_type) Spinner spinnerType;
    @BindView(R.id.et_fbd_brand_name) TextView etBrandName;
    @BindView(R.id.et_fbd_model_name) TextView etModelName;
    @BindView(R.id.et_fbd_frame_number) TextView etFrameNumber;
    @BindView(R.id.et_fbd_weight) TextView etWeight;
    @BindView(R.id.et_fbd_description) EditText etDescription;

    private BikeViewModel bikeViewModel;
    private String cameraFilePath;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bike_details, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bikeViewModel = ViewModelProviders.of(getActivity()).get(BikeViewModel.class);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.bike_type_names, R.layout.support_simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        String bikeId = bikeViewModel.getBikeId();
        bikeViewModel.getBike().observe(this, new Observer<Bike>() {
            @Override
            public void onChanged(Bike bike) {
                if (bike.getImage() != null)
                    ivPhoto.setImageBitmap(bike.getImage());
                etName.setText(bike.getName());
                spinnerType.setSelection(bike.getType());
                etBrandName.setText(bike.getBrand_name());
                etModelName.setText(bike.getModel_name());
                etFrameNumber.setText(bike.getFrame_number());
                if (bike.getWeight() == 0)
                    etWeight.setText(null);
                else
                    etWeight.setText(String.format("%.2f", bike.getWeight()));
                etDescription.setText(bike.getDescription());
            }
        });
    }

    @OnClick(R.id.btn_fbd_take_photo)
    public void onClickTakePhoto() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            dispatchPickPictureIntent();
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.PERMISSION_READ);
        }
    }

    /**
     * Starts image picker
     */
    private void dispatchPickPictureIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, Constants.REQUEST_PICK_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constants.PERMISSION_READ && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            dispatchPickPictureIntent();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_PICK_PHOTO && resultCode == RESULT_OK) {
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                bikeViewModel.saveBikeImage(bitmap);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Save all bike details when the fragment is going to be destroyed
     */
    @Override
    public void onStop() {
        super.onStop();

        String name = etName.getText().toString();
        int type = (int)spinnerType.getSelectedItemId();
        String brandName = etBrandName.getText().toString();
        String modelName = etModelName.getText().toString();
        String frameNumber = etFrameNumber.getText().toString();
        float weight = (etWeight.getText().toString().equals("")) ? 0 : Float.valueOf(etWeight.getText().toString());
        String description = etDescription.getText().toString();

        Bike bike = new Bike(name, type, brandName, modelName, frameNumber, weight, description);
        bikeViewModel.saveBike(bike);
    }

    /*@OnClick(R.id.btn_fbd_take_photo)
    public void onClickTakePhoto() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, Constants.PERMISSION_READ);
        } else {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constants.PERMISSION_READ) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, Constants.REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Bitmap bitmap = new Bitmap();//(Bitmap) data.getExtras().get("data");
            try {
                Uri uri = Uri.parse(data.getExtras().getString(MediaStore.EXTRA_OUTPUT));
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                bikeViewModel.saveBikeImage(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = bikeViewModel.getBikeId() + "_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        cameraFilePath = "file://" + image.getAbsolutePath();
        return image;
    }*/
}
