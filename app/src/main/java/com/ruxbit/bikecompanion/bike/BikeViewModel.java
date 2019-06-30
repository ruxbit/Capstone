package com.ruxbit.bikecompanion.bike;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ruxbit.bikecompanion.model.Bike;
import com.ruxbit.bikecompanion.model.Part;
import com.ruxbit.bikecompanion.model.Ride;
import com.ruxbit.bikecompanion.model.Task;

import java.util.List;

public class BikeViewModel extends AndroidViewModel {
    private BikeRepository bikeRepository;
    private LiveData<List<Part>> parts;
    private String bikeId;

    public BikeViewModel(Application application) {
        super(application);
        bikeRepository = new BikeRepository(application);
    }

    public void initBikeId(String bikeId) {
        this.bikeId = bikeId;
        this.parts = bikeRepository.loadPartsOfBikeDb(bikeId);
    }

    public String getBikeId() {
        return bikeId;
    }

    public LiveData<Bike> getBike() {
        return bikeRepository.getBike(bikeId);
    }

    public void saveBike(Bike bike) {
        bike.setId(bikeId);
        bikeRepository.saveBike(bike);
    }

    public void saveBikeImage(Bitmap bitmap) {
        bikeRepository.updateBikeImage(bikeId, bitmap);
    }

    public LiveData<List<Part>> getParts() {
        return parts;
    }

    public void addPart(Part part) {
        bikeRepository.addPart(part);
    }

    public LiveData<List<Ride>> getRides() {
        return bikeRepository.getRidesForBike(bikeId);
    }

    public LiveData<List<Task>> getAllTasks() {
        return bikeRepository.loadAllTasks();
    }

    public void deletePart(Part part) {
        bikeRepository.deletePart(part);
    }

    public void updatePart(Part part) {bikeRepository.updatePart(part);}
}
