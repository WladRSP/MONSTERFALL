package com.wnb.projetandroid;

import android.os.Handler;

public class MovingThread implements Runnable {

    Handler handler;
    int movingRate = 50;

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(movingRate);
                handler.sendEmptyMessage(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
