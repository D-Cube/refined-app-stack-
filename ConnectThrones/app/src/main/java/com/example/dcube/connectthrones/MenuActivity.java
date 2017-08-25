package com.example.dcube.connectthrones;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {
    MediaPlayer sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        sp = MediaPlayer.create(MenuActivity.this, R.raw.sword);

        ((Button) findViewById(R.id.player1)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                //Log.d("DEBUG", "One Player Button Pressed!");
                sp.start();
                Intent intent = new Intent(MenuActivity.this, ChoosesideActivity.class);
                intent.putExtra("gameType", true);
                startActivityForResult(intent, 0);
            }
        });

        ((Button) findViewById(R.id.player2)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                //Log.d("DEBUG", "Two Player Button Pressed!");
                sp.start();
                Intent intent = new Intent(MenuActivity.this, ChoosesideActivity.class);
                intent.putExtra("gameType", false);
                startActivityForResult(intent, 0);
            }
        });

        ((Button) findViewById(R.id.exit)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                //Log.d("DEBUG", "Exit Game Button Pressed!");
                sp.start();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

    }
}
