package com.ruxbit.bikecompanion.widget;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ruxbit.bikecompanion.database.AppDatabase;
import com.ruxbit.bikecompanion.database.BikeDao;
import com.ruxbit.bikecompanion.model.Bike;

import java.util.List;

public class WidgetConfigureViewModel extends AndroidViewModel {
    private LiveData<List<Bike>> bikes;
    private BikeDao bikedao;

    public WidgetConfigureViewModel(@NonNull Application application) {
        super(application);
        bikedao = AppDatabase.getInstance(application).bikeDao();
        bikes = bikedao.loadAllBikes();
    }

    public LiveData<List<Bike>> getBikes() {
        return bikes;
    }
}
