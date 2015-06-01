package com.wnb.projetandroid;

public class Player {

    // Attributes
    int damages = 10;
    int maxLife = 100;
    int currentLife;

    public Player() {
        this.currentLife = this.maxLife;
    }


    // Check if player still alive
    public boolean isAlive() {
        if(this.currentLife <= 0) {
            return false;
        }
        return true;
    }

}
