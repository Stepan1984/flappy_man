package com.banana.flappy;

import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    ImageView bg_view;
    ImageView wall_view;
    ImageView man_view;
    TextView scoreTextView;
    TextView scoreViewLose;
    TextView bestRecordView;
    RelativeLayout loseLayout;
    Button newGameButton;
    Button pauseButton;
    Button resumeButton;
    RelativeLayout pauseLayout;

    float obstacleX = 500;
    float obstacleY = 0;
    float birdY = 0;
    float birdV = 0;
    int score = 0;
    int record = 0;
    boolean started = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bg_view = findViewById(R.id.bg_view);
        scoreTextView = findViewById(R.id.score_text_view);
        wall_view = findViewById(R.id.wall_view);
        man_view = findViewById(R.id.bird_view);
        scoreViewLose = findViewById(R.id.score_view_lose);
        bestRecordView = findViewById(R.id.best_record_view);
        loseLayout = findViewById(R.id.lose_layout);
        newGameButton = findViewById(R.id.start_new_game);
        pauseButton = findViewById(R.id.pause);
        resumeButton = findViewById(R.id.resume);
        pauseLayout = findViewById(R.id.pause_layout);

        RelativeLayout rootContainer = findViewById(R.id.root_container);
        rootContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birdV = -10;
            }
        });
        record = getRecord();


        initializeTimer();
        AnimationDrawable animation = (AnimationDrawable) man_view.getBackground();
        animation.start();
        showGameOverDialog();
        Glide.with(this)
                .load("https://www.planwallpaper.com/static/images/Alien_Ink_2560X1600_Abstract_Background_1.jpg")
                .into(bg_view);
    }

    private void initializeTimer() {
        final Handler handler = new Handler();
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(started == true){
                    onTimer();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onTimerUi();
                        }
                    });
                }
            }
        }, 40, 40);
    }

    private void onTimer() {
        obstacleX -= 7.5;

        if (wall_view.getTranslationX() < -500) {
            obstacleX = 500;
            score++;
            obstacleY = (float) (Math.random() * 400 - 200);
        }
        birdV += 0.4;
        birdY += birdV;

    }

    private void onTimerUi() {
        wall_view.setTranslationX(obstacleX);
        wall_view.setTranslationY(obstacleY);
        scoreTextView.setText(String.valueOf(score));
        man_view.setTranslationY(birdY);
        if (birdY > 750 || birdY < -750) {
            showGameOverDialog();
        }
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseMenu();
            }
        });
    }

    private void showGameOverDialog() {
        started = false;

        loseLayout.setVisibility(View.VISIBLE);
        if (score > record) {
            record = score;
            saveRecord(record);
        } else if (record >= score){
            getRecord();
        }
        scoreViewLose.setText("SCORE=" + score);
        bestRecordView.setText("BEST RECORD=" + record);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameButton();
            }
        });
    }

    private void startNewGame() {
        obstacleX = 500;
        obstacleY = 0;
        birdY = 0;
        birdV = 0;
        score = 0;
        started = true;
    }

    private void saveRecord(int record) {
        // получаем штуку для сохранения данных
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        // открываем её для записи
        SharedPreferences.Editor editor = preferences.edit();
        // записываем число
        editor.putInt("РЕКОРД", record);
        // сохраняем изменения
        editor.apply();
    }
    private int getRecord() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        return preferences.getInt("РЕКОРД", 0);

    }
    private void GameButton(){
        loseLayout.setVisibility(View.GONE);
        startNewGame();
    }
    private void pauseMenu (){
        pauseLayout.setVisibility(View.VISIBLE);
        started = false;
        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             resumeGame();
            }
        });
    }
    private void resumeGame (){
        pauseLayout.setVisibility(View.GONE);
        started = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        started=false;
        pauseMenu();
    }
}
