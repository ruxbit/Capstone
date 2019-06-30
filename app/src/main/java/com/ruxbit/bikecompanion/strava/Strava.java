package com.ruxbit.bikecompanion.strava;

import android.net.Uri;

import com.ruxbit.bikecompanion.model.Ride;
import com.ruxbit.bikecompanion.model.Athlete;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Strava {
    private static final String CLIENT_ID = "";
    private static final String CLIENT_SECRET = "";
    private static final String REDIRECT_URL = "com.ruxbit.bikecompanion://oauth2redirect";
    private static final String RESPONSE_TYPE = "code";
    private static final String APPROVAL_PROMPT = "auto";
    private static final String SCOPE = "read,activity:read_all,profile:read_all";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String MAX_ITEMS_PER_PAGE = "200";
    private static final String PARAM_CODE = "code";

    public static final String AUTHORIZE_URL = "https://www.strava.com/oauth/mobile/authorize";
    public static final String ACCESS_TOKEN_URL = "https://www.strava.com/oauth/token";
    public static final String ACTIVITIES_URL = "https://www.strava.com/api/v3/athlete/activities";
    public static final String AUTHENTICATED_ATHLETE_URL = "https://www.strava.com/api/v3/athlete";

    private static final int STATE_NEED_INIT = 0;
    private static final int STATE_CONNECTED = 1;

    private static Strava instance;
    private String authorizationCode;
    private String accessToken;
    private String refreshToken;
    private Date accessTokenExpiresAt;
    private Athlete athlete;
    private List<Ride> activities;
    private int state = STATE_NEED_INIT;

    public static synchronized Strava getInstance() {
        if (instance == null) {
            instance = new Strava();
        }
        return instance;
    }

    public int getState() {
        return state;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Athlete getAthlete() {
        return athlete;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setAccessTokenExpiresAt(String accessTokenExpiresAt) {
        this.accessTokenExpiresAt = new Date((long)Integer.valueOf(accessTokenExpiresAt)*1000);
    }

    public String setAuthorizationCode(Uri uri) {
        String code = uri.getQueryParameter(PARAM_CODE);
        this.authorizationCode = code;
        return code;
    }

    public void setAuthorizationCode(String code) {
        this.authorizationCode = code;
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }

    public void setRides(List<Ride> activities) {
        this.activities = activities;
    }

    public Uri buildAuthorizationUri() {
        return Uri.parse(AUTHORIZE_URL)
                .buildUpon()
                .appendQueryParameter("client_id", CLIENT_ID)
                .appendQueryParameter("redirect_uri", REDIRECT_URL)
                .appendQueryParameter("response_type", RESPONSE_TYPE)
                .appendQueryParameter("approval_prompt", APPROVAL_PROMPT)
                .appendQueryParameter("scope", SCOPE)
                .build();
    }

    /**
     * Fetches access token
     * @throws IOException
     */
    public void fetchAccessTokenSync() throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("client_id", CLIENT_ID)
                .add("client_secret", CLIENT_SECRET)
                .add("code", authorizationCode)
                .add("grant_type", GRANT_TYPE)
                .build();
        Request request = new Request.Builder()
                .url(Strava.ACCESS_TOKEN_URL)
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();
        String jsonData = response.body().string();
        final JsonObject root = new JsonParser().parse(jsonData).getAsJsonObject();

        accessToken = root.get("access_token").getAsString();
        refreshToken = root.get("refresh_token").getAsString();
        setAccessTokenExpiresAt(root.get("expires_at").getAsString());
        state = STATE_CONNECTED;
    }


    /**
     * Fetches all rides page by page
     * @return list of rides
     * @throws IOException
     */
    public List<Ride> fetchRidesSync() throws IOException {
        OkHttpClient client = new OkHttpClient();
        List<Ride> rides = new ArrayList<>();
        Boolean hasActivities = true;
        int page = 1;

        while (hasActivities) {
            HttpUrl url = HttpUrl.parse(Strava.ACTIVITIES_URL).newBuilder()
                    .addQueryParameter("before", String.valueOf(new Date().getTime() / 1000l))
                    .addQueryParameter("after", "0")
                    .addQueryParameter("page", String.valueOf(page))
                    .addQueryParameter("per_page", MAX_ITEMS_PER_PAGE)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .get()
                    .build();

            Response response = client.newCall(request).execute();
            String jsonData = response.body().string();
            final Gson gson = new Gson();
            final JsonArray root = new JsonParser().parse(jsonData).getAsJsonArray();

            Ride[] activitiesArr = gson.fromJson(root, Ride[].class);
            if (activitiesArr.length == 0) {
                hasActivities = false;
            } else {
                rides.addAll(Arrays.asList(activitiesArr));
            }
            page++;
        }
        setRides(rides);
        return rides;
    }

    /**
     * Updates Athlete object inside Strava
     * @throws IOException
     */
    public void updateAthleteSync() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Strava.AUTHENTICATED_ATHLETE_URL)
                .addHeader("Authorization", "Bearer " + accessToken)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        String jsonData = response.body().string();
        final Gson gson = new Gson();
        final JsonObject root = new JsonParser().parse(jsonData).getAsJsonObject();

        athlete = gson.fromJson(root, Athlete.class);
    }
}
