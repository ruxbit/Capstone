package com.ruxbit.bikecompanion.bike;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;

import com.ruxbit.bikecompanion.utils.AppExecutors;
import com.ruxbit.bikecompanion.database.BikeDao;
import com.ruxbit.bikecompanion.database.RideDao;
import com.ruxbit.bikecompanion.database.TaskDao;
import com.ruxbit.bikecompanion.model.Bike;
import com.ruxbit.bikecompanion.model.Part;
import com.ruxbit.bikecompanion.database.AppDatabase;
import com.ruxbit.bikecompanion.database.PartDao;
import com.ruxbit.bikecompanion.model.Ride;
import com.ruxbit.bikecompanion.model.Task;

import java.util.List;

public class BikeRepository {
    private PartDao partDao;
    private BikeDao bikeDao;
    private RideDao rideDao;
    private TaskDao taskDao;

    public BikeRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.partDao = db.partDao();
        this.bikeDao = db.bikeDao();
        this.rideDao = db.rideDao();
        this.taskDao = db.taskDao();
    }

    public LiveData<List<Part>> loadPartsOfBikeDb(String bikeId) {
        return partDao.loadPartsOfBike(bikeId);
    }

    public LiveData<List<Task>> loadAllTasks() {
        return taskDao.loadAllTasks();
    }

    public void addPart(Part part) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                partDao.insertPart(part);
            }
        });
    }

    public LiveData<Bike> getBike(String bikeId) {
        return bikeDao.getBikeById(bikeId);
    }

    public void saveBike(Bike bike) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                bikeDao.updateBikeInfo(bike.getId(), bike.getName(), bike.getType(),
                        bike.getBrand_name(), bike.getModel_name(), bike.getFrame_number(),
                        bike.getWeight(), bike.getDescription());
            }
        });
    }

    public void updateBikeImage(String bikeId, Bitmap bitmap) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                bikeDao.updateBikeImage(bikeId, bitmap);
            }
        });
    }

    public LiveData<List<Ride>> getRidesForBike(String bikeId) {
        return rideDao.getRidesForBike(bikeId);
    }

    public void deletePart(Part part) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                partDao.deletePart(part);
            }
        });
    }

    public void updatePart(Part part) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                partDao.updatePart(part.getId(), part.getType(), part.getManufacturer(), part.getModel());
            }
        });
    }
}

