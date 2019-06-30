package com.ruxbit.bikecompanion.utils;

public class LoadingState {
    public static final int INACTIVE = 100;
    public static final int LOADING = 101;
    public static final int SUCCESS = 102;

    public static final int ERROR_FETCH_ACCESS_TOKEN = 0;
    public static final int ERROR_FETCH_RIDES = 1;
    public static final int ERROR_FETCH_BIKES = 2;

    private int state;

    public LoadingState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
}
