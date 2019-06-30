package com.ruxbit.bikecompanion.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ruxbit.bikecompanion.model.Ride;

import java.util.List;

@Dao
public abstract class RideDao {
    @Query("SELECT * FROM ride WHERE gear_id=:gear_id ORDER BY start_date")
    public abstract LiveData<List<Ride>> getRidesForBike(String gear_id);

    @Query("DELETE FROM ride")
    public abstract void deleteRides();

    @Insert
    public abstract void insertRides(List<Ride> rides);

    @Transaction
    public void updateRides(List<Ride> rides) {
        deleteRides();
        insertRides(rides);
    }
}