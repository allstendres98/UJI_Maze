package com.al375502.ujimaze;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import com.al375502.ujimaze.mazeUtils.Direction;
import com.al375502.ujimaze.mazeUtils.Maze;
import com.al375502.ujimaze.mazeUtils.Position;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import es.uji.vj1229.framework.Graphics;
import es.uji.vj1229.framework.IGameController;
import es.uji.vj1229.framework.TouchHandler;

public class Controller implements IGameController {
    private static final float MARGIN_FRACTION = 0.08f;
    private static final float LINEWIDTH_FRACTION = 0.01f;
    private static final float CELL_FRACTION = (1 - 2 * MARGIN_FRACTION - 2 * LINEWIDTH_FRACTION)/7;
    private static final int BACKGROUND_COLOR = 0xff9ffccf;
    private static final int LINE_COLOR = 0xff000000;
    private static final int SUBLINE_COLOR = 0xffb8b8b8;
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
    float x0 = 0, y0 = 0;
    boolean tocando = false;
    Direction directionToGo;

    public Controller(int width, int height, Context context) {
        this.width = width;
        this.height = height;
        this.context = context;
        playerSide = (int) (width * CELL_FRACTION);
        lineWidth = (int) (width * LINEWIDTH_FRACTION);
        configureGraphicsParameters(width,height);
        Assets.createPlayerAssets(context,playerSide);
        Assets.createTargetAssets(context,playerSide);
        graphics = new Graphics(this.width, this.height);
        model = new Model(cellX,cellY);
    }

    @Override
    public void onUpdate(float deltaTime, List<TouchHandler.TouchEvent> touchEvents) {
        //Log.d("targets", "");
        for(TouchHandler.TouchEvent event : touchEvents) {
            if (event.type == TouchHandler.TouchType.TOUCH_UP) {
                if (xreset <= event.x && event.x <= xreset + BUTTON_SIZE && yreset <= event.y && event.y <= yreset + BUTTON_SIZE) {
                    model.resetMaze();
                }
                else if (xundo <= event.x && event.x <= xundo + BUTTON_SIZE && yundo <= event.y && event.y <= yundo + BUTTON_SIZE) {
                    model.goToPreviousPosition();
                } else if (xhelp <= event.x && event.x <= xhelp + BUTTON_SIZE && yhelp <= event.y && event.y <= yhelp + BUTTON_SIZE) {
                    //Log.d("pressed", "help");
                } else {
                    if(!model.playerIsMoving) {
                        float y1 = event.y, x1 = event.x;
                        //Log.d("pressed", "x0: " + x0 + " y0: " + y0 + " x1: " + x1 + " y1: " + y1);
                        if (x0 < x1 && y0 < y1) {
                            if (Math.abs(x0 - x1) > Math.abs(y0 - y1))
                                directionToGo = Direction.RIGHT;
                            else directionToGo = Direction.DOWN;
                        }

                        if (x0 > x1 && y0 < y1) {
                            if (Math.abs(x0 - x1) > Math.abs(y0 - y1))
                                directionToGo = Direction.LEFT;
                            else directionToGo = Direction.DOWN;
                        }

                        if (x0 < x1 && y0 > y1) {
                            if (Math.abs(x0 - x1) > Math.abs(y0 - y1))
                                directionToGo = Direction.RIGHT;
                            else directionToGo = Direction.UP;
                        }

                        if (x0 > x1 && y0 > y1) {
                            if (Math.abs(x0 - x1) > Math.abs(y0 - y1))
                                directionToGo = Direction.LEFT;
                            else directionToGo = Direction.UP;
                        }
                        model.calculateNextPosition(directionToGo);
                    }
                }
                tocando = false;
            }

            if(event.type == TouchHandler.TouchType.TOUCH_DOWN)
            {
                if(!tocando)
                {
                    tocando=true;
                    x0 = event.x;
                    y0 = event.y;
                    //Log.d("pressed", "x0: " + x0 + " y0: " + y0);
                }
            }

        }
        if(model.playerIsMoving) model.startMovingDirection(directionToGo, deltaTime);
    }

    @Override
    public Bitmap onDrawingRequested() {
        graphics.clear(BACKGROUND_COLOR);
        drawAssets();
        drawPlayer();
        drawMaze();
        return graphics.getFrameBuffer();
    }

    private void drawPlayer() {
        graphics.drawBitmap(Assets.playerDown,model.playerCurrentPositionX,model.playerCurrentPositionY);
    }

    public void configureGraphicsParameters(int width, int height)
    {
        xoffset = (width - 7 * playerSide - 2*lineWidth) / 2;
        yoffset = (height - 7 * playerSide - 2*lineWidth) / 2;

        float step = playerSide + lineWidth/3.5f;
        cellX = new float[8];
        cellY = new float[8];

        for (int i = 0; i < 7; i++){
            cellX[i] = xoffset + i*step;
            cellY[i] = yoffset + i*step;
        }
        cellX[7] = width-xoffset;
        cellY[7] = height-yoffset;

        xhelp = width/2+width/6;
        yhelp = height/2-height/2.5f;
        xundo = width/2 - width/2.8f;
        yundo = height/2-height/2.5f;
        xreset = width/2-width/10;
        yreset = height/2-height/2.5f;
    }

    private void drawAssets(){
        graphics.drawDrawable(Assets.help,xhelp,yhelp,BUTTON_SIZE,BUTTON_SIZE);
        graphics.drawDrawable(Assets.reset,xreset, yreset,BUTTON_SIZE,BUTTON_SIZE);
        graphics.drawDrawable(Assets.undo,xundo,yundo,BUTTON_SIZE,BUTTON_SIZE);

    }

    private void drawMaze(){
        Maze maze = Levels.mazes[model.getCurrentMaze()];
        for(int i = 0; i < maze.getNRows(); i++)
            for(int j = maze.getNCols(); j >= 0; j--) {
                graphics.drawLine(cellX[i], cellY[i], cellX[j], cellY[i], lineWidth, SUBLINE_COLOR);
                graphics.drawLine(cellX[i], cellY[i], cellX[i], cellY[j], lineWidth, SUBLINE_COLOR);
                graphics.drawLine(cellX[i], cellY[j], cellX[j], cellY[j], lineWidth, SUBLINE_COLOR);
                graphics.drawLine(cellX[j], cellY[i], cellX[j], cellY[j], lineWidth, SUBLINE_COLOR);
            }

        char[] row = maze.toString().toCharArray();
        int cont = 0;
        int aux = 0;
        for(int i = 0; i < maze.toString().length(); i++,aux++)
        {
            if(Character.toString(row[i]).equals("-")){
                graphics.drawLine(cellX[(aux-1)/2], cellY[cont/2], cellX[(aux+1)/2], cellY[cont/2], lineWidth, LINE_COLOR);
            }
            else if(Character.toString(row[i]).equals("|")){
                graphics.drawLine(cellX[aux/2], cellY[(cont-1)/2], cellX[aux/2], cellY[(cont+1)/2], lineWidth, LINE_COLOR);
            }
            else if(Character.toString(row[i]).equals("\n")){
                cont++;
                aux = 0;
            }
        }
        for(int i = 0; i < model.targets.length; i++)
        {
            if(!model.targetsCollected[i]) graphics.drawBitmap(Assets.target0,cellX[model.targets[i].getCol()],cellY[model.targets[i].getRow()]);
            else graphics.drawBitmap(Assets.target0,-900,-900);
        }
    }
}
