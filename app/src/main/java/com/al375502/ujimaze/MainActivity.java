package com.al375502.ujimaze;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;

import es.uji.vj1229.framework.GameActivity;
import es.uji.vj1229.framework.IGameController;

public class MainActivity extends GameActivity{

    Controller controller;
    @Override
    protected IGameController buildGameController() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        controller = new Controller(displayMetrics.widthPixels, displayMetrics.heightPixels, getApplicationContext());
        return controller;
    }

    @Override
    protected void onPause() {
        super.onPause();
        controller.stopMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        controller.resumeMusic();
    }
}