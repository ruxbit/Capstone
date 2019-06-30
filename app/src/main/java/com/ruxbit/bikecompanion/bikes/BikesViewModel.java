package com.ruxbit.bikecompanion.bikes;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ruxbit.bikecompanion.model.Bike;
import com.ruxbit.bikecompanion.utils.Constants;
import com.ruxbit.bikecompanion.utils.LoadingState;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class BikesViewModel extends AndroidViewModel {
    private BikesRepository bikesRepository;
    private LiveData<List<Bike>> bikes;
    private MutableLiveData<LoadingState> loadingState = new MutableLiveData<>();

    public LiveData<List<Bike>> getBikes() {
        return bikes;
    }

    public MutableLiveData<LoadingState> getLoadingState() {
        return loadingState;
    }

    public BikesViewModel(Application application) {
        super(application);
        bikesRepository = new BikesRepository(application, loadingState);
        bikes = bikesRepository.loadBikesDb();
    }

    public void updateBikes() {
        SharedPreferences prefs = getApplication().getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        int state = prefs.getInt(Constants.PREF_TAG_STATE, Constants.PREF_STATE_FIRST_LAUNCH);
        switch (state) {
            case Constants.PREF_STATE_HAS_CODE:
                String code = prefs.getString(Constants.PREF_TAG_CODE, null);
                bikesRepository.setAuthorizationCode(code);
                break;
        }
    }

    public void setAuthorizationCodeFromUri(Context context, Uri uri) {
        bikesRepository.setAuthorizationCodeFromUri(context, uri);
    }
}
