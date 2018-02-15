package com.banana.flappy;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
ImageView bg_view;
ImageView wall_view;
ImageView man_view;

    float obstacleX = 500;
    float obstacleY = 0;
    float birdY = 0;
    float birdV = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bg_view = findViewById(R.id.bg_view);
        wall_view = findViewById(R.id.wall_view);
        man_view = findViewById(R.id.bird_view);
        RelativeLayout rootContainer = findViewById(R.id.root_container);
        rootContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birdV = -10;
            }
        });


        initializeTimer();
    }
    private void initializeTimer() {
        final Handler handler = new Handler();
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                onTimer();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onTimerUi();
                    }
                });
            }
        }, 40, 40);
    }

    private void onTimer() {
        obstacleX -= 7.5;

        if(wall_view.getTranslationX() < -500){
            obstacleX = 500;
            obstacleY = (float) (Math.random()*400-200);
        }
        birdV += 0.4;
        birdY += birdV;
        if (birdY > 750 || birdY < -750){
            startNewGame();
        }

    }

    private void onTimerUi() {
        wall_view.setTranslationX(obstacleX);
        wall_view.setTranslationY(obstacleY);

        man_view.setTranslationY(birdY);
    }
    private void startNewGame(){
        obstacleX = 500;
        obstacleY = 0;
        birdY = 0;
        birdV = 0;
    }
}
