package com.al375502.ujimaze;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.List;

import es.uji.vj1229.framework.IGameController;
import es.uji.vj1229.framework.TouchHandler;

public class Controller implements IGameController {
    public int width, height;
    public Context context;
    public Model model;

    public Controller(int width, int height, Context context) {
        this.width = width;
        this.height = height;
        this.context = context;
    }

    @Override
    public void onUpdate(float deltaTime, List<TouchHandler.TouchEvent> touchEvents) {

    }

    @Override
    public Bitmap onDrawingRequested() {
        return null;
    }
}
