package com.ruxbit.bikecompanion.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruxbit.bikecompanion.R;
import com.ruxbit.bikecompanion.strava.Strava;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.iv_ab_strava_login) ImageView ivLogin;
    @BindView(R.id.tv_ab_hint_login) TextView tvLogin;
    @BindView(R.id.cl_ab_login) ConstraintLayout clLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.iv_ab_strava_login)
    public void onClickStravaLogin(View view) {
        Uri authorizationUri = Strava.getInstance().buildAuthorizationUri();
        Intent intent = new Intent(Intent.ACTION_VIEW, authorizationUri);
        startActivity(intent);
    }
}
