package com.wnb.projetandroid;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.widget.*;
import android.view.*;
import android.util.AttributeSet;

import java.util.ArrayList;


import static android.graphics.Color.TRANSPARENT;

public class GameLayout extends View {

    // Attributes
    ArrayList<Monster> monsters = new ArrayList<Monster>();
    int scaleX = 1;
    int scaleY = 1;
    Game game;
    int maxPlayerLife;
    int currentPlayerLife;

    // View components
    TextView scoreBar;
    ProgressBar lifeBar;
    TextView levelText;
    TextView lifeText;

    // Constructor
    public GameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // Methods
    public void addMonster(Monster monster){
        monsters.add(monster);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(TRANSPARENT);
        Resources res = getResources();
        this.scaleX = this.getWidth()/8;
        this.scaleY = this.scaleX;
        for(Monster monster : this.monsters) {
            Bitmap bitmapToResize = null;
            Bitmap monsterImage   = null;
            switch (monster.monsterId){
                case 1 :
                    bitmapToResize = BitmapFactory.decodeResource(res, R.drawable.sp1);
                    monsterImage   = getResizedBitmap(bitmapToResize, scaleX, scaleY);
                    break;
                case 2 :
                    bitmapToResize = BitmapFactory.decodeResource(res, R.drawable.sp2);
                    monsterImage   = getResizedBitmap(bitmapToResize, scaleX, scaleY);
                    break;
                case 3 :
                    bitmapToResize  = BitmapFactory.decodeResource(res, R.drawable.sp3);
                    monsterImage    = getResizedBitmap(bitmapToResize, scaleX, scaleY);
                    break;
                case 4 :
                    bitmapToResize  = BitmapFactory.decodeResource(res, R.drawable.sp4);
                    monsterImage    = getResizedBitmap(bitmapToResize, scaleX, scaleY);
                    break;
                case 5 :
                    bitmapToResize  = BitmapFactory.decodeResource(res, R.drawable.sp5);
                    monsterImage    = getResizedBitmap(bitmapToResize, scaleX, scaleY);
                    break;
                default:
                    bitmapToResize = BitmapFactory.decodeResource(res, R.drawable.sp1);
                    monsterImage   = getResizedBitmap(bitmapToResize, scaleX, scaleY);
                break;
            }
            if(monster.isBoss){
                if(monster.life > 0){
                    bitmapToResize = BitmapFactory.decodeResource(res, R.drawable.boss);
                    monsterImage = getResizedBitmap(bitmapToResize, scaleX * 2, scaleY * 2 );
                }
            }
            canvas.drawBitmap(monsterImage, monster.x, monster.y, null);
        }

    }

    // Function to summon monsters
    public void spawnMonster() {

        // Randomize monsters spawn position
        int layoutWidth = (int)this.getWidth();
        int t1 = (int)(0.5*layoutWidth/10);
        int t2 = (int)(2.5*layoutWidth/10);
        int t3 = (int)(4.5*layoutWidth/10);
        int t4 = (int)(6.5*layoutWidth/10);
        int t5 = (int)(8.5*layoutWidth/10);

        ArrayList<Integer> monstersList = new ArrayList();

        monstersList.add(t1);
        monstersList.add(t2);
        monstersList.add(t3);
        monstersList.add(t4);
        monstersList.add(t5);
        // Ajout d'un 6ème élément pour éviter le crash dans le cas ou random() rend exactement 1
        monstersList.add(t5);

        int index = (int) (Math.random()* (5 - 0) + 0);
        int monsterX = monstersList.get(index);

        // Randomize monsters image
        int monsterId = (int)(Math.random() * (6-1)) + 1;
        this.addMonster(new Monster(monsterId, monsterX , -100, 1, false, 10));
    }

    // Function to destroy monster when tap on it
    public void destroyMonster(MotionEvent e, Player player) {
        Monster monsterToBeRemoved = null;
        for(Monster monster : this.monsters) {
            if(!monster.isBoss) {
                if (monster.y - this.scaleY / 2 < e.getY() && e.getY() < monster.y + this.scaleY * 1.5
                        && monster.x - this.scaleX / 2 < e.getX() && e.getX() < monster.x + this.scaleX * 1.5) {
                    monster.life -= player.damages;
                    if (monster.life <= 0) {
                        monsterToBeRemoved = monster;
                    }
                    break;
                }
            }
            else{
                if (monster.y - this.scaleY < e.getY() && e.getY() < monster.y + this.scaleY * 3
                        && monster.x - this.scaleX < e.getX() && e.getX() < monster.x + this.scaleX * 3) {
                    monster.life -= player.damages;
                    if (monster.life <= 0) {
                        monsterToBeRemoved = monster;
                    }
                    break;
                }
            }
        }
        if(monsterToBeRemoved != null && !monsterToBeRemoved.isBoss) {
            this.monsters.remove(monsterToBeRemoved);
            this.game.score += (int)(10*(1+(float)(this.game.level-1)/10));
            this.scoreBar.setText(String.valueOf(this.game.score));
            this.game.levelKilledMonsters++;
            this.game.totalKilledMonsters++;
            this.game.isEndLevel();
        }
        if(monsterToBeRemoved != null && monsterToBeRemoved.isBoss) {
            this.monsters.remove(monsterToBeRemoved);
            this.game.score += (int)(20*(1+(float)(this.game.level-1)/10));
            this.scoreBar.setText(String.valueOf(this.game.score));
            this.game.levelKilledMonsters++;
            this.game.totalKilledMonsters++;
            this.game.isEndLevel();
        }

    }

    public void spawnBoss(int level){
        int layoutWidth = (int)this.getWidth();
        int bossX = (int)(4.5*layoutWidth/10);
        Monster boss = new Monster(2, bossX, -100, level * 3, true, 20);
        this.monsters.add(boss);
    }


    // Move monsters to a percentage of screen height
    public void moveMonsters() {
        int layoutHeigth = (int)this.getHeight();
        int delta        = (int)(layoutHeigth/100);
        int deltaBoss    = (int)(layoutHeigth/400);
        this.currentPlayerLife = this.lifeBar.getProgress();
        Monster monsterToBeRemoved = null;
        for(Monster monster : this.monsters) {
            monster.y += monster.isBoss ? deltaBoss : delta;
            if(monster.y > this.getHeight()) {
                monsterToBeRemoved = monster;
            }
        }
        if(monsterToBeRemoved != null) {
            this.monsters.remove(monsterToBeRemoved);
            this.lifeBar.setProgress(this.currentPlayerLife - monsterToBeRemoved.damages);
            this.game.player.currentLife = this.currentPlayerLife - monsterToBeRemoved.damages;
            this.lifeText.setText(
                Integer.toString(this.game.player.currentLife) + "/" + Integer.toString(this.game.player.maxLife)
            );
        }
        this.invalidate();
    }

    // To clear monster's arraylist
    public void clearMonsterList() {
        this.monsters.clear();
    }

    // Resize bitmap to make monsters looks bigger
    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {

        int width  = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth  = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;

    }
}
