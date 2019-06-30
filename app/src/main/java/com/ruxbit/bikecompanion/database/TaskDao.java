package com.ruxbit.bikecompanion.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ruxbit.bikecompanion.model.Task;

import java.util.List;

@Dao
public abstract class TaskDao {
    @Query("SELECT * FROM task WHERE partId=:partId ORDER BY id")
    public abstract LiveData<List<Task>> loadTasksOfPart(int partId);

    @Query("SELECT * FROM task ORDER BY id")
    public abstract LiveData<List<Task>> loadAllTasks();

    @Query("UPDATE task " +
            "SET progress = ifnull((SELECT SUM(distance) FROM ride WHERE task.dateStart < ride.start_date AND ride.gear_id = :bikeId), 0) " +
            "WHERE task.id = :taskId")
    public abstract void updateDistanceForTask(String bikeId, int taskId);

    @Query("UPDATE task " +
            "SET progress = ifnull((SELECT SUM(ride.moving_time) FROM ride WHERE task.dateStart < ride.start_date AND ride.gear_id = :bikeId), 0) " +
            "WHERE task.id = :taskId")
    public abstract void updateTimeForTask(String bikeId, int taskId);

    @Query("UPDATE task " +
            "SET progress = ifnull((SELECT SUM(ride.distance) FROM ride WHERE task.dateStart < ride.start_date AND EXISTS (SELECT * FROM part WHERE ride.gear_id = part.bikeId AND task.partId = part.id )), 0)" +
            "WHERE task.units = 0")
    public abstract void updateDistanceForAllTasks();

    @Query("UPDATE task " +
            "SET progress = ifnull((SELECT SUM(ride.moving_time) FROM ride WHERE task.dateStart < ride.start_date AND EXISTS (SELECT * FROM part WHERE ride.gear_id = part.bikeId AND task.partId = part.id )), 0)" +
            "WHERE task.units = 1")
    public abstract void updateTimeForAllTasks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insertTask(Task task);

    @Delete
    public abstract void deleteTask(Task task);
}

