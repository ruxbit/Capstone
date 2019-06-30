package com.ruxbit.bikecompanion.tasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.ruxbit.bikecompanion.utils.Constants;
import com.ruxbit.bikecompanion.R;
import com.ruxbit.bikecompanion.model.Part;
import com.ruxbit.bikecompanion.model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TasksActivity extends AppCompatActivity implements TasksAdapter.TasksAdapterOnClickHandler, AddTaskDialogFragment.AddTaskDialogListener {
    @BindView(R.id.rv_at_tasks) RecyclerView rvTasks;
    @BindView(R.id.fab_at_add_task) FloatingActionButton fabAddTask;
    @BindView(R.id.tv_at_part_name) TextView tvPartName;

    private TasksViewModel tasksViewModel;
    private TasksAdapter tasksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        ButterKnife.bind(this);

        Part part = Parcels.unwrap(getIntent().getExtras().getParcelable(Constants.INTENT_PART));

        tvPartName.setText(part.getManufacturer() + " " + part.getModel());

        tasksAdapter = new TasksAdapter(this, this.getResources());
        rvTasks.setAdapter(tasksAdapter);
        rvTasks.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        TasksViewModelFactory tasksViewModelFactory = new TasksViewModelFactory(getApplication(), part);
        tasksViewModel = ViewModelProviders.of(this, tasksViewModelFactory).get(TasksViewModel.class);
        tasksViewModel.getTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                tasksAdapter.setTasksData(tasks);
            }
        });
    }

    @OnClick(R.id.fab_at_add_task)
    public void onClickAddTask() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddTaskDialogFragment f = AddTaskDialogFragment.newInstance(tasksViewModel.getPart().getId());
        f.show(fragmentManager, "dialog_fragment_add_task");
    }

    @Override
    public void onClickDelete(Task task) {
        tasksViewModel.deleteTask(task);
    }

    @Override
    public void onClickEdit(Task task) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddTaskDialogFragment f = AddTaskDialogFragment.newInstance(task);
        f.show(fragmentManager, "dialog_fragment_add_task");
    }

    @Override
    public void onSaveTask(Task task) {
        tasksViewModel.addTask(task);
    }
}