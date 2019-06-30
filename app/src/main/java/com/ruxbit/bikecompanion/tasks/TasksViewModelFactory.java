package com.ruxbit.bikecompanion.tasks;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ruxbit.bikecompanion.model.Part;

public class TasksViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final Application application;
    private final Part part;

    public TasksViewModelFactory(Application application, Part part) {
        this.application = application;
        this.part = part;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TasksViewModel(application, part);
    }
}
