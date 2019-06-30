package com.ruxbit.bikecompanion.bikes;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ruxbit.bikecompanion.utils.AppExecutors;
import com.ruxbit.bikecompanion.utils.Constants;
import com.ruxbit.bikecompanion.database.RideDao;
import com.ruxbit.bikecompanion.database.TaskDao;
import com.ruxbit.bikecompanion.model.Bike;
import com.ruxbit.bikecompanion.database.AppDatabase;
import com.ruxbit.bikecompanion.database.BikeDao;
import com.ruxbit.bikecompanion.model.Ride;
import com.ruxbit.bikecompanion.strava.Strava;
import com.ruxbit.bikecompanion.utils.LoadingState;

import java.io.IOException;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class BikesRepository {
    private BikeDao bikeDao;
    private RideDao rideDao;
    private TaskDao taskDao;
    private MutableLiveData<LoadingState> loadingState;

    public BikesRepository(Application application, MutableLiveData<LoadingState> loadingState) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.bikeDao = db.bikeDao();
        this.rideDao = db.rideDao();
        this.taskDao = db.taskDao();
        this.loadingState = loadingState;
    }

    public LiveData<List<Bike>> loadBikesDb() {
        return bikeDao.loadAllBikes();
    }

    public void updateAllDataAsync() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                loadingState.postValue(new LoadingState(LoadingState.LOADING));

                try {
                    Strava.getInstance().fetchAccessTokenSync();
                } catch (IOException e) {
                    loadingState.postValue(new LoadingState(LoadingState.ERROR_FETCH_ACCESS_TOKEN));
                    return;
                }

                try {
                    List<Ride> rides = Strava.getInstance().fetchRidesSync();
                    rideDao.updateRides(rides);
                } catch (IOException e) {
                    loadingState.postValue(new LoadingState(LoadingState.ERROR_FETCH_RIDES));
                    return;
                }

                try {
                    Strava.getInstance().updateAthleteSync();
                    bikeDao.upsertBikes(Strava.getInstance().getAthlete().getBikes());
                } catch (IOException e) {
                    loadingState.postValue(new LoadingState(LoadingState.ERROR_FETCH_BIKES));
                    return;
                }


                taskDao.updateDistanceForAllTasks();
                taskDao.updateTimeForAllTasks();

                loadingState.postValue(new LoadingState(LoadingState.SUCCESS));

                    /*taskDao.resetAllProgress();
                    for (Ride ride : rides) {
                        taskDao.updateAllProgress(ride.getGear_id(), Task.UNIT_KM, ride.getDistance(), ride.getStart_date());
                        taskDao.updateAllProgress(ride.getGear_id(), Task.UNIT_H, ride.getMoving_time(), ride.getStart_date());
                    }*/
            }
        });
    }

    public void setAuthorizationCodeFromUri(Context context, Uri uri) {
        String code = Strava.getInstance().setAuthorizationCode(uri);
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE).edit();
        editor.putInt(Constants.PREF_TAG_STATE, Constants.PREF_STATE_HAS_CODE);
        editor.putString(Constants.PREF_TAG_CODE, code);
        editor.apply();

        updateAllDataAsync();
    }

    public void setAuthorizationCode(String code) {
        Strava.getInstance().setAuthorizationCode(code);
        updateAllDataAsync();
    }
}
