package com.ruxbit.bikecompanion.bike.parts;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ruxbit.bikecompanion.utils.UnitConverter;
import com.ruxbit.bikecompanion.model.Part;
import com.ruxbit.bikecompanion.R;
import com.ruxbit.bikecompanion.model.Task;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PartsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEWTYPE_PART = 0;
    public static final int VIEWTYPE_TASK = 1;
    private List<Part> parts;
    private List<Task> tasks;
    private List<Object> data;

    private final PartsAdapter.PartsAdapterOnClickHandler clickHandler;
    private final Resources resources;

    public interface PartsAdapterOnClickHandler {
        void onClick(Part part);
        void onDelete(Part part);
        void onEdit(Part part);
    }

    public PartsAdapter(PartsAdapter.PartsAdapterOnClickHandler clickHandler, Resources resources) {
        this.clickHandler = clickHandler;
        this.resources = resources;
    }

    /**
     * ViewHolder for Parts
     */
    public class PartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_lip_part) ImageView ivPart;
        @BindView(R.id.tv_lip_type) TextView tvType;
        @BindView(R.id.tv_lip_manufacturer_name) TextView tvManufacturer;
        @BindView(R.id.tv_lip_model_name) TextView tvModel;

        public PartViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            clickHandler.onClick((Part)data.get(adapterPosition));
        }

        @OnClick(R.id.ib_lip_delete)
        public void onClickDelete() {
            int adapterPosition = getAdapterPosition();
            clickHandler.onDelete((Part)data.get(adapterPosition));
        }

        @OnClick(R.id.ib_lip_edit)
        public void onClickEdit() {
            int adapterPosition = getAdapterPosition();
            clickHandler.onEdit((Part)data.get(adapterPosition));
        }
    }

    /**
     * ViewHolder for Tasks
     */
    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.pb_lipt_progress) ProgressBar pbProgress;
        @BindView(R.id.tv_lipt_progress) TextView tvProgress;
        @BindView(R.id.tv_lipt_task_name) TextView tvTaskName;

        public TaskViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            while (data.get(adapterPosition) instanceof Task) {
                adapterPosition--;
            }
            clickHandler.onClick((Part)data.get(adapterPosition));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View root;
        switch (viewType) {
            case VIEWTYPE_PART:
                root = inflater.inflate(R.layout.list_item_part, parent, false);
                return new PartViewHolder(root);
            default:
                root = inflater.inflate(R.layout.list_item_part_task, parent ,false);
                return new TaskViewHolder(root);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case VIEWTYPE_PART:
                PartViewHolder partViewHolder = (PartViewHolder)viewHolder;
                Part part = (Part)data.get(position);
                TypedArray imgs = resources.obtainTypedArray(R.array.part_type_icons);
                partViewHolder.ivPart.setImageResource(imgs.getResourceId(part.getType(), 0));

                TypedArray types = resources.obtainTypedArray(R.array.part_type_names);
                partViewHolder.tvType.setText(types.getString(part.getType()));

                partViewHolder.tvManufacturer.setText(part.getManufacturer());
                partViewHolder.tvModel.setText(part.getModel());
                imgs.recycle();
                types.recycle();
                break;
            case VIEWTYPE_TASK:
                TaskViewHolder taskViewHolder = (TaskViewHolder)viewHolder;
                Task task = (Task)data.get(position);

                taskViewHolder.tvTaskName.setText(task.getName());

                float progress = task.getProgress();
                float interval = task.getInterval();
                String wear;

                taskViewHolder.pbProgress.setMax(Math.round(interval));
                //ObjectAnimator.ofInt(partsAdapterViewHolder.pbProgress, "progress", Math.round(progress)).setDuration(300).start();
                taskViewHolder.pbProgress.setProgress(Math.round(progress));

                if (task.getUnits() == Task.UNIT_KM) {
                    progress = UnitConverter.metersToKm(progress);
                    interval = UnitConverter.metersToKm(interval);
                    wear = resources.getString(R.string.wear, progress, interval, resources.getString(R.string.km));
                } else {
                    progress = UnitConverter.secondsToHours(progress);
                    interval = UnitConverter.secondsToHours(interval);
                    wear = resources.getString(R.string.wear, progress, interval, resources.getString(R.string.h));
                }
                taskViewHolder.tvProgress.setText(wear);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        } else {
            return data.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position) instanceof Part)
            return VIEWTYPE_PART;
        if (data.get(position) instanceof Task)
            return VIEWTYPE_TASK;
        return super.getItemViewType(position);
    }

    public void setPartsData(List<Part> parts) {
        this.parts = parts;
        if (tasks != null)
            updateData();
    }

    public void setTasksData(List<Task> tasks) {
        this.tasks = tasks;
        if (parts != null)
            updateData();
    }

    /**
     * Merges parts and tasks into a single array
     */
    private void updateData() {
        data = new ArrayList<Object>();
        for (Part part : parts) {
            data.add(part);
            for (Task task : tasks) {
                if (task.getPartId() == part.getId()) {
                    data.add(task);
                }
            }
        }
        notifyDataSetChanged();
    }
}