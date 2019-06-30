package com.ruxbit.bikecompanion.database;

import androidx.room.Dao;
import androidx.room.Query;

import com.ruxbit.bikecompanion.model.WidgetItem;

import java.util.List;

@Dao
public abstract class WidgetItemDao {
    @Query("SELECT bike.name AS bike_name, part.manufacturer AS part_manufacturer, part.model AS part_model, task.name AS task_name, task.progress, task.interval, task.units " +
            "FROM task " +
            "INNER JOIN part on part.id = task.partId " +
            "INNER JOIN bike on bike.id = part.bikeId " +
            "WHERE bike.id IN (:bikeIds) " +
            "ORDER BY task.progress / task.interval DESC")
    public abstract List<WidgetItem> getWidgetItems(List<String> bikeIds);
}
