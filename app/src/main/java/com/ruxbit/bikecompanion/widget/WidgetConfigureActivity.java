package com.ruxbit.bikecompanion.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ruxbit.bikecompanion.R;
import com.ruxbit.bikecompanion.model.Bike;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * The configuration screen for the {@link TasksWidget TasksWidget} AppWidget.
 */
public class WidgetConfigureActivity extends AppCompatActivity {
    @BindView(R.id.lv_wc_bikes) ListView lvBikes;
    @BindView(R.id.btn_wc_bikes) Button btnAdd;

    private static final String PREFS_NAME = "com.example.bikecompanion.widget.TasksWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";

    private WidgetConfigureViewModel viewModel;
    private List<CheckBox> checkBoxes;
    private List<String> ids;
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @OnClick(R.id.btn_wc_bikes)
    public void onClickAdd() {
        final Context context = WidgetConfigureActivity.this;

        // When the button is clicked, store the string locally
        savePref(context, mAppWidgetId, getCheckedJson());

        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        //TasksWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);
        appWidgetManager.notifyAppWidgetViewDataChanged(mAppWidgetId, R.id.lv_w_tasks);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    public WidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void savePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static List<String> loadPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String idsJson = prefs.getString(PREF_PREFIX_KEY + appWidgetId, "");
        Gson gson = new Gson();
        List<String> ids = Arrays.asList(gson.fromJson(idsJson, String[].class));
        return ids;
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.widget_configure);
        ButterKnife.bind(this);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        viewModel = ViewModelProviders.of(this).get(WidgetConfigureViewModel.class);
        viewModel.getBikes().observe(this, new Observer<List<Bike>>() {
            @Override
            public void onChanged(List<Bike> bikes) {
                setAdapter(bikes);
            }
        });

    }

    private void setAdapter(List<Bike> bikes) {
        checkBoxes = new ArrayList<>();
        ids = new ArrayList<>();
        for (Bike bike : bikes) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(bike.getName());
            checkBoxes.add(checkBox);
            ids.add(bike.getId());
        }
        lvBikes.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return ids == null ? 0 : ids.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (checkBoxes.get(position) == null) {
                    parent.addView(checkBoxes.get(position));
                }
                return checkBoxes.get(position);
            }
        });
    }

    private String getCheckedJson() {
        List<String> checkedIds = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            if (checkBoxes.get(i).isChecked())
                checkedIds.add(ids.get(i));
        }
        Gson gson = new Gson();
        return gson.toJson(checkedIds);
    }
}

