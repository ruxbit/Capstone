package com.ruxbit.bikecompanion.tasks;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ruxbit.bikecompanion.R;
import com.ruxbit.bikecompanion.utils.UnitConverter;
import com.ruxbit.bikecompanion.model.Task;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksAdapterViewHolder> {
    private List<Task> tasks;

    private final TasksAdapter.TasksAdapterOnClickHandler clickHandler;
    private final Resources resources;

    public interface TasksAdapterOnClickHandler {
        void onClickDelete(Task task);
        void onClickEdit(Task task);
    }

    public TasksAdapter(TasksAdapter.TasksAdapterOnClickHandler clickHandler, Resources resources) {
        this.clickHandler = clickHandler;
        this.resources = resources;
    }

    public class TasksAdapterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_lit_task_name) TextView tvTaskName;
        @BindView(R.id.tv_lit_perform_every) TextView tvPerformEvery;
        @BindView(R.id.tv_lit_start_date) TextView tvStartDate;
        @BindView(R.id.pb_lit_progress) ProgressBar pbProgress;
        @BindView(R.id.tv_lit_progress) TextView tvProgress;
        @BindView(R.id.ib_lit_edit) ImageButton ibEdit;
        @BindView(R.id.ib_lit_delete) ImageButton ibDelete;


        public TasksAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.ib_lit_delete)
        public void onClickDelete() {
            Task task = tasks.get(getAdapterPosition());
            clickHandler.onClickDelete(task);
        }

        @OnClick(R.id.ib_lit_edit)
        public void onClickEdit() {
            Task task = tasks.get(getAdapterPosition());
            clickHandler.onClickEdit(task);
        }
    }

    @Override
    public TasksAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        int layoutIdForListItem = R.layout.list_item_task;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new TasksAdapter.TasksAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TasksAdapterViewHolder tasksAdapterViewHolder, int position) {
        Task task = tasks.get(position);
        tasksAdapterViewHolder.tvTaskName.setText(task.getName());


        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");
        String strDate = simpleDate.format(task.getDateStart());
        tasksAdapterViewHolder.tvStartDate.setText(resources.getString(R.string.start_date, strDate));


        float progress = task.getProgress();
        float interval = task.getInterval();
        String wear;
        String performEvery;

        tasksAdapterViewHolder.pbProgress.setMax(Math.round(interval));
        ObjectAnimator.ofInt(tasksAdapterViewHolder.pbProgress, "progress", Math.round(progress)).setDuration(300).start();
        //tasksAdapterViewHolder.pbProgress.setProgress(Math.round(progress));

        if (task.getUnits() == Task.UNIT_KM) {
            progress = UnitConverter.metersToKm(progress);
            interval = UnitConverter.metersToKm(interval);
            wear = resources.getString(R.string.wear, progress, interval, resources.getString(R.string.km));
            performEvery = resources.getString(R.string.perform_every_i_u, Math.round(interval), resources.getString(R.string.km));
        } else {
            progress = UnitConverter.secondsToHours(progress);
            interval = UnitConverter.secondsToHours(interval);
            wear = resources.getString(R.string.wear, progress, interval, resources.getString(R.string.h));
            performEvery = resources.getString(R.string.perform_every_i_u, Math.round(interval), resources.getString(R.string.h));
        }
        tasksAdapterViewHolder.tvProgress.setText(wear);
        tasksAdapterViewHolder.tvPerformEvery.setText(performEvery);
    }

    @Override
    public int getItemCount() {
        if (tasks == null) {
            return 0;
        } else {
            return tasks.size();
        }
    }

    public void setTasksData(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public List<Task> getTasksData() {
        return tasks;
    }
}