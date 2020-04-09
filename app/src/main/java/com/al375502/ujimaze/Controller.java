package com.al375502.ujimaze;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.List;

import es.uji.vj1229.framework.Graphics;
import es.uji.vj1229.framework.IGameController;
import es.uji.vj1229.framework.TouchHandler;

public class Controller implements IGameController {
    private static final float MARGIN_FRACTION = 0.1f;
    private static final float LINEWIDTH_FRACTION = 0.02f;
    private static final float CELL_FRACTION = (1 - 2 * MARGIN_FRACTION - 2 * LINEWIDTH_FRACTION)/7;
    private static final int BACKGROUND_COLOR = 0xff9ffccf;
    private static final int LINE_COLOR = 0xff000000;
    private static final int BUTTON_SIZE = 80;

    public int playerSide;
    public int width, height;
    public Context context;
    public Model model;
    public Graphics graphics;
    public int xoffset, yoffset;
    public float xreset, yreset, xundo, yundo, xhelp, yhelp;
    public float[] cellX, cellY;
    public int lineWidth;

    public Controller(int width, int height, Context context) {
        this.width = width;
        this.height = height;
        this.context = context;
        playerSide = (int) (width * CELL_FRACTION);
        lineWidth = (int) (width * LINEWIDTH_FRACTION);
        configureGraphicsParameters(width,height);
        Assets.createPlayerAssets(context,playerSide*3);
        Assets.createTargetAssets(context,playerSide*2);
        graphics = new Graphics(this.width, this.height);
    }

    @Override
    public void onUpdate(float deltaTime, List<TouchHandler.TouchEvent> touchEvents) {
        for(TouchHandler.TouchEvent event : touchEvents)
            if(event.type == TouchHandler.TouchType.TOUCH_UP){
                if(xreset <= event.x && event.x <= xreset + BUTTON_SIZE && yreset <= event.y && event.y <= yreset + BUTTON_SIZE)
                    //aqui iria para reiniciar la partida
                    Log.d("press:", "reset");
                else if(xundo <= event.x && event.x <= xundo + BUTTON_SIZE && yundo <= event.y && event.y <= yundo + BUTTON_SIZE){
                    Log.d("press:", "undo");
                }
                else if(xhelp <= event.x && event.x <= xhelp + BUTTON_SIZE && yhelp <= event.y && event.y <= yhelp + BUTTON_SIZE){
                    Log.d("press:", "help");
                }
                else{
                    //todo lo del movimiento
                }
            }
    }

    @Override
    public Bitmap onDrawingRequested() {
        graphics.clear(BACKGROUND_COLOR);
        drawAssets();
        drawMaze();
        return graphics.getFrameBuffer();
    }

    public void configureGraphicsParameters(int width, int height)
    {
        xoffset = (width - 7 * playerSide - 2 * lineWidth) / 2;
        yoffset = (height - 7 * playerSide - 2 * lineWidth) / 2;

        float step = playerSide + lineWidth;
        cellX = new float[8];
        cellY = new float[8];

        for (int i = 0; i < 7; i++){
            cellX[i] = xoffset + i*step;
            cellY[i] = yoffset + i*step;
            //Log.d("Cell", cellX[i] + " " + cellY[i]);
        }
        cellX[7] = xoffset + 7*playerSide + 2*lineWidth;
        cellY[7] = yoffset + 7*playerSide + 2*lineWidth;

        xhelp = width/2+width/6;
        yhelp = height/2-height/2.5f;
        xundo = width/2 - width/2.8f;
        yundo = height/2-height/2.5f;
        xreset = width/2-width/10;
        yreset = height/2-height/2.5f;
    }

    private void drawAssets(){
        //for(int row = 0; row < 7; row++)
          //  for(int col = 0; col < 7; col++)
                //graphics.drawBitmap(Assets.playerLeft,cellX[row],cellY[col]);
        /*graphics.drawBitmap(Assets.playerDown,0,0);
        graphics.drawBitmap(Assets.playerLeft,0,0);
        graphics.drawBitmap(Assets.playerRight,0,0);
        graphics.drawBitmap(Assets.playerUp,0,0);*/

        graphics.drawDrawable(Assets.help,xhelp,yhelp,BUTTON_SIZE,BUTTON_SIZE);
        graphics.drawDrawable(Assets.reset,xreset, yreset,BUTTON_SIZE,BUTTON_SIZE);
        graphics.drawDrawable(Assets.undo,xundo,yundo,BUTTON_SIZE,BUTTON_SIZE);

    }

    private void drawMaze(){
        float halfLineWidth = 0.5f * lineWidth;

        graphics.drawLine(cellX[0], cellY[0], cellX[7], cellY[0], lineWidth, LINE_COLOR);
        graphics.drawLine(cellX[0] , cellY[0], cellX[0], cellY[7], lineWidth, LINE_COLOR);
        graphics.drawLine(cellX[0] , cellY[7], cellX[7], cellY[7], lineWidth, LINE_COLOR);
        graphics.drawLine(cellX[7] , cellY[0], cellX[7], cellY[7], lineWidth, LINE_COLOR);
    }
}
