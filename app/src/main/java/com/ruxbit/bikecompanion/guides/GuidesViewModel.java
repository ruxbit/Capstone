package com.ruxbit.bikecompanion.guides;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ruxbit.bikecompanion.model.Guide;

import java.util.List;

public class GuidesViewModel extends AndroidViewModel {
    private GuidesRepository guidesRepository;
    private MutableLiveData<List<Guide>> guides;

    public GuidesViewModel(@NonNull Application application) {
        super(application);
        guidesRepository = new GuidesRepository();
        guides = new MutableLiveData<>();
        guidesRepository.getGuides(guides);
    }

    public LiveData<List<Guide>> getGuides() {
        return guides;
    }
}
