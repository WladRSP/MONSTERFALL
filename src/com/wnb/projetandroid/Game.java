package com.wnb.projetandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.*;
import android.view.*;
import android.widget.*;

public class Game extends Activity {

    // Threads & Handlers
    MovingThread timerMoving     = new MovingThread();
    SpawningThread timerSpawning = new SpawningThread();

    Handler handlerMoving;
    Handler handlerSpawning;
    Player player;

    // Layout components
    GameLayout gameLayout;

    // Other activities
    Intent gameOver;

    // Attributes
    int score = 0;
    boolean isRunnig;
    boolean isOnGameOver;
    int level = 1;
    int bossSpawned = 0;
    int levelKilledMonsters = 0;
    int totalKilledMonsters = 0;
    int totalKilledBoss = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int uiOptions =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_FULLSCREEN |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.game);

        this.player = new Player();

        this.gameLayout = (GameLayout)findViewById(R.id.GameLayout);
        this.gameLayout.scoreBar  = (TextView)findViewById(R.id.scoreBar);
        this.gameLayout.lifeBar   = (ProgressBar)findViewById(R.id.lifeBar);
        this.gameLayout.levelText = (TextView)findViewById(R.id.levelText);
        this.gameLayout.lifeText  = (TextView)findViewById(R.id.lifeText);

        this.gameLayout.lifeText.setText(Integer.toString(
            this.player.currentLife) + "/" + Integer.toString(this.player.maxLife)
        );

        this.gameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                switch(e.getAction()) {
                    case MotionEvent.ACTION_DOWN :
                        gameLayout.destroyMonster(e, player);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        this.isOnGameOver = false;
        this.isRunnig     = true;
        this.gameLayout.game = this;

        this.gameLayout.maxPlayerLife     = this.player.maxLife;
        this.gameLayout.currentPlayerLife = this.player.maxLife;
        this.gameLayout.lifeBar.setMax(this.player.maxLife);
        this.gameLayout.lifeBar.setProgress(this.player.maxLife);

        // Threads management
        this.handlerMoving = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(isRunnig) {
                    gameLayout.moveMonsters();
                    if(!player.isAlive()) {
                        if(!isOnGameOver) {
                            isOnGameOver = true;
                            gameOver();
                        }
                    }
                }
            }
        };

        this.handlerSpawning = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(isRunnig) {
                    gameLayout.spawnMonster();
                    if(level % 2 == 0 && bossSpawned < level / 2){
                        bossSpawned++;
                        gameLayout.spawnBoss(level);
                    }
                }
            }
        };

        // Thread initializations
        this.timerSpawning.handler = this.handlerSpawning;
        this.timerMoving.handler   = this.handlerMoving;
        final Thread tSpawning = new Thread(this.timerSpawning);
        final Thread tMoving   = new Thread(this.timerMoving);
        tMoving.start();
        tSpawning.start();
    }

    // Leveling
    public void isEndLevel(){
        if(this.levelKilledMonsters >= (10*(1+(float)(this.level-1)/10))) {
            this.levelKilledMonsters = 0;
            this.level++;
            this.gameLayout.levelText.setText("Level " + String.valueOf(this.level));
            if (this.timerSpawning.spawnRate > 100) {
                this.timerSpawning.spawnRate -= 50;
            }
            if(this.timerMoving.movingRate > 10) {
                this.timerMoving.movingRate -= 4;
            }
        }
    }

    // GameOver
    public void gameOver() {
        this.isRunnig = false;
        this.gameOver = new Intent(Game.this, GameOver.class);
        this.gameOver.putExtra("score", Integer.toString(this.score));
        this.gameOver.putExtra("maxLevel", Integer.toString(this.level));
        this.gameOver.putExtra("killedMonsters", Integer.toString(this.totalKilledMonsters));
        this.gameLayout.clearMonsterList();
        startActivity(gameOver);
        finish();
    }

}
