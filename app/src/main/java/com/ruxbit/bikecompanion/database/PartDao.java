package com.ruxbit.bikecompanion.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ruxbit.bikecompanion.model.Part;

import java.util.List;

@Dao
public abstract class PartDao {
    @Query("SELECT * FROM part WHERE bikeId=:bikeId ORDER BY id")
    public abstract LiveData<List<Part>> loadPartsOfBike(String bikeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertPart(Part part);

    @Delete
    public abstract void deletePart(Part part);

    @Query("UPDATE part SET type=:partType, manufacturer=:manufacturer, model=:model WHERE id=:partId")
    public abstract void updatePart(int partId, int partType, String manufacturer, String model);
}
