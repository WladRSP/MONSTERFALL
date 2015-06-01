package com.wnb.projetandroid;

public class Monster {

    int monsterId;
    int x;
    int y;
    int damages;
    int life = 1;
    boolean isBoss;

    public Monster() {}

    public Monster(int monsterId, int x, int y, int life, boolean isBoss, int damages) {
        this.monsterId =  monsterId;
        this.x = x;
        this.y = y;
        this.life = life;
        this.isBoss = isBoss;
        this.damages = damages;
    }

}
