package com.example.dcube.connectthrones;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.ybq.android.spinkit.SpinKitView;

public class SplashActivity extends AppCompatActivity {
    MediaPlayer mp , sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final SpinKitView spinKitView = (SpinKitView) findViewById(R.id.spin_kit);

        mp = MediaPlayer.create(this, R.raw.freedonbg);
        sp = MediaPlayer.create(SplashActivity.this, R.raw.sword);
        mp.start();

        new CountDownTimer(10000 , 1000) {

            @Override
            public void onTick(long l) {
                SpinKitView spinKitView = (SpinKitView) findViewById(R.id.spin_kit);
                spinKitView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                spinKitView.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(getApplicationContext() , MenuActivity.class);
                startActivity(intent);

                sp.start();
                mp.stop();

            }
        }.start();

    }
}
