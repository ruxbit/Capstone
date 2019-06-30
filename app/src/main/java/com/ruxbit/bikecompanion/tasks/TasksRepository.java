package com.ruxbit.bikecompanion.tasks;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.ruxbit.bikecompanion.utils.AppExecutors;
import com.ruxbit.bikecompanion.database.AppDatabase;
import com.ruxbit.bikecompanion.database.TaskDao;
import com.ruxbit.bikecompanion.model.Part;
import com.ruxbit.bikecompanion.model.Task;

import java.util.List;

public class TasksRepository {
    private TaskDao taskDao;

    public TasksRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.taskDao = db.taskDao();
    }

    public LiveData<List<Task>> loadTasksOfPartDb(int partId) {
        return taskDao.loadTasksOfPart(partId);
    }

    public void addTask(Task task, Part part) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                int newTaskId = (int)taskDao.insertTask(task);
                if (task.getUnits() == Task.UNIT_KM)
                    taskDao.updateDistanceForTask(part.getBikeId(), newTaskId);
                else
                    taskDao.updateTimeForTask(part.getBikeId(), newTaskId);
            }
        });
    }

    public void deleteTask(Task task) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                taskDao.deleteTask(task);
            }
        });
    }
}
