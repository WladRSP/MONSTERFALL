package com.wnb.projetandroid;

import android.os.Handler;

public class SpawningThread implements Runnable {

    //Attributes
    int spawnRate = 1000;

    Handler handler;

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(spawnRate);
                handler.sendEmptyMessage(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
