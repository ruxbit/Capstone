package com.ruxbit.bikecompanion.database;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ruxbit.bikecompanion.model.Bike;

import java.util.List;

@Dao
public abstract class BikeDao {
    @Query("SELECT * FROM bike ORDER BY id")
    public abstract LiveData<List<Bike>> loadAllBikes();

    @Query("SELECT * FROM bike WHERE id = :bikeId")
    public abstract LiveData<Bike> getBikeById(String bikeId);

    @Insert
    public abstract void insertBike(Bike bike);

    @Delete
    public abstract void deleteBike(Bike bike);

    @Query("UPDATE bike SET image=:image WHERE id=:id")
    public abstract void updateBikeImage(String id, Bitmap image);

    @Query("UPDATE bike SET name=:name, type=:type, brand_name=:brand_name, model_name=:model_name, frame_number=:frame_number, weight=:weight, description=:description WHERE id=:id")
    public abstract void updateBikeInfo(String id, String name, int type, String brand_name, String model_name, String frame_number, float weight, String description);

    @Query("UPDATE bike SET name=:name WHERE id=:id")
    public abstract void updateBikeName(String id, String name);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract List<Long> insertBikes(List<Bike> bikes);

    /**
     * Adds new bikes if they don't exist locally
     * Updates bikes' names if they exist locally
     * @param bikes
     */
    @Transaction
    public void upsertBikes(List<Bike> bikes) {
        List<Long> insertResult = insertBikes(bikes);
        for (int i = 0; i < insertResult.size(); i++) {
            if (insertResult.get(i) == -1) {
                updateBikeName(bikes.get(i).getId(), bikes.get(i).getName());
            }
        }
    }
}
