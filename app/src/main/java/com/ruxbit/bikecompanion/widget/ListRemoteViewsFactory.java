package com.ruxbit.bikecompanion.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.ruxbit.bikecompanion.R;
import com.ruxbit.bikecompanion.database.AppDatabase;
import com.ruxbit.bikecompanion.database.WidgetItemDao;
import com.ruxbit.bikecompanion.model.Task;
import com.ruxbit.bikecompanion.model.WidgetItem;
import com.ruxbit.bikecompanion.utils.UnitConverter;

import java.util.List;

public class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private int appWidgetId;
    private WidgetItemDao widgetItemDao;
    private List<WidgetItem> widgetItems;

    public ListRemoteViewsFactory(Context context, int appWidgetId) {
        this.context = context;
        this.appWidgetId = appWidgetId;
    }

    @Override
    public void onCreate() {
        AppDatabase db = AppDatabase.getInstance(context);
        widgetItemDao = db.widgetItemDao();
    }

    @Override
    public void onDataSetChanged() {
        List<String> ids = WidgetConfigureActivity.loadPref(context, appWidgetId);
        widgetItems = widgetItemDao.getWidgetItems(ids);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return widgetItems == null ? 0 : widgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        WidgetItem item = widgetItems.get(position);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);
        views.setTextViewText(R.id.tv_wli_bike, item.bike_name);
        views.setTextViewText(R.id.tv_wli_part, item.part_manufacturer + " " + item.part_model);
        views.setTextViewText(R.id.tv_wli_task, item.task_name);
        views.setProgressBar(R.id.pb_wli_progress, item.interval, Math.round(item.progress),false);

        float progress = item.progress;
        float interval = item.interval;
        String wear;
        if (item.units == Task.UNIT_KM) {
            progress = UnitConverter.metersToKm(progress);
            interval = UnitConverter.metersToKm(interval);
            wear = context.getResources().getString(R.string.wear, progress, interval, context.getResources().getString(R.string.km));
        } else {
            progress = UnitConverter.secondsToHours(progress);
            interval = UnitConverter.secondsToHours(interval);
            wear = context.getResources().getString(R.string.wear, progress, interval, context.getResources().getString(R.string.h));
        }
        views.setTextViewText(R.id.tv_wli_progress, wear);

        views.setOnClickFillInIntent(R.id.rl_wli_container, new Intent());

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
