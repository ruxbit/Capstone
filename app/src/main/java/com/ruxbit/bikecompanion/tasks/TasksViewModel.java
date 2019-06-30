package com.ruxbit.bikecompanion.tasks;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ruxbit.bikecompanion.model.Part;
import com.ruxbit.bikecompanion.model.Task;

import java.util.List;

public class TasksViewModel extends AndroidViewModel {
    private TasksRepository tasksRepository;
    private LiveData<List<Task>> tasks;
    private Part part;

    public TasksViewModel(Application application, Part part) {
        super(application);
        this.part = part;
        tasksRepository = new TasksRepository(application);
        tasks = tasksRepository.loadTasksOfPartDb(part.getId());
    }

    public LiveData<List<Task>> getTasks() {
        return tasks;
    }

    public void addTask(Task task) {
        tasksRepository.addTask(task, part);
    }

    public Part getPart() {
        return part;
    }

    public void deleteTask(Task task) {
        tasksRepository.deleteTask(task);
    }
}