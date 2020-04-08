package com.al375502.ujimaze;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;

import es.uji.vj1229.framework.GameActivity;
import es.uji.vj1229.framework.IGameController;

public class MainActivity extends GameActivity{

    @Override
    protected IGameController buildGameController() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return new Controller(displayMetrics.widthPixels, displayMetrics.heightPixels, getApplicationContext());
    }
}