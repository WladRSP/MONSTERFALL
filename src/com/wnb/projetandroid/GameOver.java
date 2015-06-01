package com.wnb.projetandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOver extends Activity {

    // View components
    Button btnRestart;
    Button btnHome;
    Button btnExit;

    TextView totalScore;
    TextView reachedLevel;
    TextView monstersKilled;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int uiOptions =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.gameover);

        this.btnRestart = (Button)findViewById(R.id.btnRestart);
        this.btnHome    = (Button)findViewById(R.id.btnHome);
        this.btnExit    = (Button)findViewById(R.id.btnExit);

        this.btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent game = new Intent(GameOver.this, Game.class);
                startActivity(game);
                finish();
            }
        });

        this.btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(GameOver.this, Home.class);
                startActivity(home);
                finish();
            }
        });

        this.btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        this.totalScore     = (TextView)findViewById(R.id.totalScore);
        this.reachedLevel   = (TextView)findViewById(R.id.reachedLevel);
        this.monstersKilled = (TextView)findViewById(R.id.killedMonsters);

        String score          = getIntent().getStringExtra("score");
        String maxLevel       = getIntent().getStringExtra("maxLevel");
        String killedMonsters = getIntent().getStringExtra("killedMonsters");

        this.totalScore.setText("Score : " + score);
        this.reachedLevel.setText("Reached level : " + maxLevel);
        this.monstersKilled.setText("Total killed monsters : " + killedMonsters);
    }

}
