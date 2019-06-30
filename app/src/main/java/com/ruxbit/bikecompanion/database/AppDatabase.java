package com.ruxbit.bikecompanion.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ruxbit.bikecompanion.model.Bike;
import com.ruxbit.bikecompanion.model.Part;
import com.ruxbit.bikecompanion.model.Ride;
import com.ruxbit.bikecompanion.model.Task;

@Database(entities = {Bike.class, Part.class, Task.class, Ride.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "app_database";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    public abstract BikeDao bikeDao();
    public abstract PartDao partDao();
    public abstract TaskDao taskDao();
    public abstract RideDao rideDao();
    public abstract WidgetItemDao widgetItemDao();
}
