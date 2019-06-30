package com.ruxbit.bikecompanion.tasks;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ruxbit.bikecompanion.utils.Constants;
import com.ruxbit.bikecompanion.R;
import com.ruxbit.bikecompanion.utils.UnitConverter;
import com.ruxbit.bikecompanion.model.Task;

import org.parceler.Parcels;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddTaskDialogFragment extends DialogFragment {
    @BindView(R.id.et_task_name) EditText etTaskName;
    @BindView(R.id.et_task_interval) EditText etTaskInterval;
    @BindView(R.id.rg_task_units) RadioGroup rgTaskUnits;
    @BindView(R.id.btn_task_calendar) Button btnTaskCalendar;
    @BindView(R.id.rb_task_hours) RadioButton rbTaskHours;
    @BindView(R.id.rb_task_km) RadioButton rbTaskKm;
    @BindView(R.id.tv_task_date) TextView tvTaskDate;
    @BindView(R.id.btn_save_task) Button btnTaskSave;

    private int taskId;
    private int partId;
    private Date dateStart = new Date();

    public AddTaskDialogFragment() {

    }

    public interface AddTaskDialogListener {
        void onSaveTask(Task task);
    }


    public static AddTaskDialogFragment newInstance(int partId) {
        AddTaskDialogFragment f = new AddTaskDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.TAG_PART_ID, partId);
        f.setArguments(bundle);
        return f;
    }

    public static AddTaskDialogFragment newInstance(Task task) {
        AddTaskDialogFragment f = new AddTaskDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.TAG_TASK, Parcels.wrap(task));
        f.setArguments(bundle);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_add_task, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (getArguments().containsKey(Constants.TAG_TASK)) {
            Task task = Parcels.unwrap(getArguments().getParcelable(Constants.TAG_TASK));
            taskId = task.getId();
            partId = task.getPartId();
            etTaskName.setText(task.getName());
            if (task.getUnits() == Task.UNIT_KM)
                etTaskInterval.setText(String.valueOf(Math.round(UnitConverter.metersToKm(task.getInterval()))));
            else
                etTaskInterval.setText(String.valueOf(Math.round(UnitConverter.secondsToHours(task.getInterval()))));
            ((RadioButton)rgTaskUnits.getChildAt(task.getUnits())).setChecked(true);
            dateStart = task.getDateStart();
            tvTaskDate.setText(DateFormat.getDateInstance().format(dateStart));
        } else {
            partId = getArguments().getInt(Constants.TAG_PART_ID);
            tvTaskDate.setText(DateFormat.getDateInstance().format(dateStart));
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                dateStart = calendar.getTime();
                tvTaskDate.setText(DateFormat.getDateInstance().format(dateStart));
            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        Calendar calendar = Calendar.getInstance();
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        calendar.add(Calendar.YEAR, -20);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        btnTaskCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        etTaskName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                validateName();
            }
        });

        etTaskInterval.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                validateInterval();
            }
        });
    }

    /**
     * Sets error on etTaskName if input is empty
     * @return false if valid
     */
    private Boolean validateName() {
        if (etTaskName.getText().toString().equals("")) {
            etTaskName.setError(getContext().getResources().getString(R.string.task_name_empty));
            return true;
        } else {
            etTaskName.setError(null);
            return false;
        }
    }

    /**
     * Sets error on etTaskInterval if input is empty
     * @return false if valid
     */
    private Boolean validateInterval() {
        if (etTaskInterval.getText().toString().equals("")) {
            etTaskInterval.setError(getContext().getResources().getString(R.string.task_interval_empty));
            return true;
        } else {
            etTaskInterval.setError(null);
            return false;
        }
    }

    @OnClick(R.id.btn_save_task)
    public void onClickSaveTask() {
        if (validateName() || validateInterval())
            return;

        String name = etTaskName.getText().toString();
        int radioButtonID = rgTaskUnits.getCheckedRadioButtonId();
        View radioButton = rgTaskUnits.findViewById(radioButtonID);
        int units = rgTaskUnits.indexOfChild(radioButton);
        int interval = Integer.valueOf(etTaskInterval.getText().toString());
        if (units == Task.UNIT_KM)
            interval = UnitConverter.kmToMeters(interval);
        else
            interval = UnitConverter.hoursToSeconds(interval);
        Task task = new Task(taskId, partId, name, units, interval, dateStart);

        AddTaskDialogListener listener = (AddTaskDialogListener) getActivity();
        listener.onSaveTask(task);
        dismiss();
    }

    @OnClick(R.id.btn_cancel_task)
    public void onClickCancelTask() {
        dismiss();
    }
}
