package com.al375502.ujimaze;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.Builder;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import com.al375502.ujimaze.mazeUtils.Direction;
import com.al375502.ujimaze.mazeUtils.Maze;
import com.al375502.ujimaze.mazeUtils.Position;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import es.uji.vj1229.framework.AnimatedBitmap;
import es.uji.vj1229.framework.Graphics;
import es.uji.vj1229.framework.IGameController;
import es.uji.vj1229.framework.TouchHandler;

public class Controller implements IGameController, Model.SoundPlayer {
    private static final float MARGIN_FRACTION = 0.08f;
    private static final float LINEWIDTH_FRACTION = 0.01f;
    private static final float CELL_FRACTION = (1 - 2 * MARGIN_FRACTION - 2 * LINEWIDTH_FRACTION)/7;
    private static final int BACKGROUND_COLOR = 0xffb8b8b8;
    private static final int LINE_COLOR = 0xff000000;
    private static final int SUBLINE_COLOR = 0xffb8b8b8;

    private AnimatedBitmap playerUP;
    private AnimatedBitmap playerDOWN;
    private AnimatedBitmap playerLEFT;
    private AnimatedBitmap playerRIGHT;

    private AnimatedBitmap targetAnimated;

    private AnimatedBitmap enemieUP;
    private AnimatedBitmap enemieDOWN;
    private AnimatedBitmap enemieLEFT;
    private AnimatedBitmap enemieRIGHT;

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
    //Music
    private SoundPool soundPool;
    private MediaPlayer mediaPlayer;
    private int Move;
    private int TargetCollected;
    private int AllTargetsCollected;
    private int TouchWall;
    private int Reset;
    private float BUTTON_SIZE;

    public Controller(int width, int height, Context context) {
        this.width = width;
        this.height = height;
        this.context = context;
        prepareSoundPool(context);
        playerSide = (int) (width * CELL_FRACTION);
        lineWidth = (int) (width * LINEWIDTH_FRACTION);
        configureGraphicsParameters(width,height);
        Assets.createPlayerAssets(context,playerSide);
        Assets.createTargetAssets(context,playerSide);
        Assets.createWallsAssets(context,playerSide);
        Assets.createGrassAssets(context,playerSide, lineWidth);
        Assets.createEnemiesAssets(context,playerSide);
        definePlayerAnimationAssets();
        defineTargetAnimationAssets();
        defineEnemieAnimationAssets();
        graphics = new Graphics(this.width, this.height);
        prepareMediaPlayer();
        model = new Model(cellX,cellY,this, mediaPlayer);
    }

    private void defineEnemieAnimationAssets() {
        enemieUP = new AnimatedBitmap(0.5f, Assets.enemieUp0, Assets.enemieUp1, Assets.enemieUp2, Assets.enemieUp3);
        enemieDOWN = new AnimatedBitmap(0.5f, Assets.enemieDown0, Assets.enemieDown1, Assets.enemieDown2, Assets.enemieDown3);
        enemieLEFT = new AnimatedBitmap(0.5f, Assets.enemieLeft0, Assets.enemieLeft1, Assets.enemieLeft2, Assets.enemieLeft3);
        enemieRIGHT = new AnimatedBitmap(0.5f, Assets.enemieRight0, Assets.enemieRight1, Assets.enemieRight2, Assets.enemieRight3);
    }

    private void defineTargetAnimationAssets() {
        targetAnimated = new AnimatedBitmap(1f, Assets.target0, Assets.target1, Assets.target2, Assets.target3, Assets.target4, Assets.target5);
    }

    private void definePlayerAnimationAssets() {
        playerUP = new AnimatedBitmap(0.5f, Assets.playerUp0, Assets.playerUp1, Assets.playerUp2, Assets.playerUp3);
        playerDOWN = new AnimatedBitmap(0.5f, Assets.playerDown0, Assets.playerDown1, Assets.playerDown2, Assets.playerDown3);
        playerLEFT = new AnimatedBitmap(0.5f, Assets.playerLeft0, Assets.playerLeft1, Assets.playerLeft2, Assets.playerLeft3);
        playerRIGHT = new AnimatedBitmap(0.5f, Assets.playerRight0, Assets.playerRight1, Assets.playerRight2, Assets.playerRight3);
    }

    private void prepareMediaPlayer() {
        mediaPlayer = MediaPlayer.create(context, R.raw.background_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(0.1f,0.1f);
    }


    @Override
    public void onUpdate(float deltaTime, List<TouchHandler.TouchEvent> touchEvents) {
        for(TouchHandler.TouchEvent event : touchEvents) {
            if (event.type == TouchHandler.TouchType.TOUCH_UP) {
                if (xreset <= event.x && event.x <= xreset + BUTTON_SIZE && yreset <= event.y && event.y <= yreset + BUTTON_SIZE) {
                    model.touchResetButton();
                }
                else if (xundo <= event.x && event.x <= xundo + BUTTON_SIZE && yundo <= event.y && event.y <= yundo + BUTTON_SIZE) {
                    if(!model.playerIsMoving) model.goToPreviousPosition();
                } else if (xhelp <= event.x && event.x <= xhelp + BUTTON_SIZE && yhelp <= event.y && event.y <= yhelp + BUTTON_SIZE) {
                   // model.Dijsktra();
                } else {
                    if(!model.playerIsMoving) {
                        float y1 = event.y, x1 = event.x;
                        Log.d("pressed", "x0 - x1: " + Math.abs(x0 - x1) + " y0 - y1: " + Math.abs(y0 - y1));
                        if(Math.abs(x0 - x1) > 80f || Math.abs(y0 - y1) > 80f){
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
                            if(directionToGo!= null) model.calculateNextPosition(directionToGo);
                        }

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
        animatePlayerTargetEnemie(deltaTime);
        model.moveEnemies(deltaTime);
    }

    private void animatePlayerTargetEnemie(float deltaTime) {
        playerUP.update(deltaTime);
        playerDOWN.update(deltaTime);
        playerRIGHT.update(deltaTime);
        playerLEFT.update(deltaTime);
        targetAnimated.update(deltaTime);
        enemieUP.update(deltaTime);
        enemieRIGHT.update(deltaTime);
        enemieLEFT.update(deltaTime);
        enemieDOWN.update(deltaTime);
    }

    @Override
    public Bitmap onDrawingRequested() {
        graphics.clear(BACKGROUND_COLOR);
        drawAssets();
        drawMaze();
        drawPlayer();
        drawTargets();
        drawEnemies();
        return graphics.getFrameBuffer();
    }

    private void drawEnemies() {
        for(int i = 0; i < model.enemies.length; i++)
        {
            if(model.enemieDirectionToGo[i] == Direction.DOWN) graphics.drawBitmap(enemieDOWN.getCurrentFrame(), model.enemieX[i], model.enemieY[i]);
            if(model.enemieDirectionToGo[i] == Direction.LEFT) graphics.drawBitmap(enemieLEFT.getCurrentFrame(), model.enemieX[i], model.enemieY[i]);
            if(model.enemieDirectionToGo[i] == Direction.RIGHT) graphics.drawBitmap(enemieRIGHT.getCurrentFrame(), model.enemieX[i], model.enemieY[i]);
            if(model.enemieDirectionToGo[i] == Direction.UP) graphics.drawBitmap(enemieUP.getCurrentFrame(), model.enemieX[i], model.enemieY[i]);
        }
    }

    private void drawTargets() {
        for(int i = 0; i < model.targets.length; i++)
        {
            if(!model.targetsCollected[i]) graphics.drawBitmap(targetAnimated.getCurrentFrame(),cellX[model.targets[i].getCol()],cellY[model.targets[i].getRow()]);
        }
    }

    private void drawPlayer() {
        if(model.playerIsMoving)
        {
            if(directionToGo == Direction.RIGHT) graphics.drawBitmap(playerRIGHT.getCurrentFrame(),model.playerCurrentPositionX,model.playerCurrentPositionY);
            if(directionToGo == Direction.DOWN) graphics.drawBitmap(playerDOWN.getCurrentFrame(),model.playerCurrentPositionX,model.playerCurrentPositionY);
            if(directionToGo == Direction.UP) graphics.drawBitmap(playerUP.getCurrentFrame(),model.playerCurrentPositionX,model.playerCurrentPositionY);
            if(directionToGo == Direction.LEFT) graphics.drawBitmap(playerLEFT.getCurrentFrame(),model.playerCurrentPositionX,model.playerCurrentPositionY);
        }
        else graphics.drawBitmap(Assets.playerDown0,model.playerCurrentPositionX,model.playerCurrentPositionY);
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
        BUTTON_SIZE = playerSide*1.5f;
    }

    private void drawAssets(){
        graphics.drawDrawable(Assets.help,xhelp,yhelp,BUTTON_SIZE,BUTTON_SIZE);
        graphics.drawDrawable(Assets.reset,xreset, yreset,BUTTON_SIZE,BUTTON_SIZE);
        graphics.drawDrawable(Assets.undo,xundo,yundo,BUTTON_SIZE,BUTTON_SIZE);
    }

    private void drawMaze(){
        Maze maze = Levels.mazes[model.getCurrentMaze()];
        for(int i = 0; i < maze.getNRows(); i++)
            for(int j = maze.getNCols()-1; j >= 0; j--) {
                /*graphics.drawLine(cellX[i], cellY[i], cellX[j], cellY[i], lineWidth, SUBLINE_COLOR);
                graphics.drawLine(cellX[i], cellY[i], cellX[i], cellY[j], lineWidth, SUBLINE_COLOR);
                graphics.drawLine(cellX[i], cellY[j], cellX[j], cellY[j], lineWidth, SUBLINE_COLOR);
                graphics.drawLine(cellX[j], cellY[i], cellX[j], cellY[j], lineWidth, SUBLINE_COLOR);*/

                graphics.drawBitmap(Assets.grass, cellX[i], cellY[j]);
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
        /*
        char[] row = maze.toString().toCharArray();
        int toPaint = 0;
        int cont = 0;
        int aux = 0;
        for(int i = 15; i < maze.toString().length(); i++, aux++)
        {
            if(!Character.toString(row[i]).equals(" ")) {
                if (Character.toString(row[i]).equals("+")) {
                    if (Character.toString(row[i - 1]).equals("-"))
                    {
                        toPaint = 13;
                        //if()
                    }
                }
                else if (Character.toString(row[i]).equals("-"))
                {
                    toPaint = 1;
                    //if()
                }
                else if (Character.toString(row[i]).equals("|"))
                {
                    toPaint = 0;
                    //if()
                }
                else if(Character.toString(row[i]).equals("\n")){
                    cont++;
                    aux = 0;
                }
                graphics.drawBitmap(Assets.wallsBitmaps[toPaint], cellX[Math.round(aux/2)], cellY[Math.round(cont/2)]);


            }


        }

         */
    }

    private void prepareSoundPool(Context context){
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder().setMaxStreams(5).setAudioAttributes(attributes).build();
        Move = soundPool.load(context, R.raw.move, 0);
        TargetCollected = soundPool.load(context, R.raw.target_collected, 0);
        AllTargetsCollected = soundPool.load(context, R.raw.all_targets_collected, 0);
        TouchWall = soundPool.load(context, R.raw.touch_wall, 0);
        Reset = soundPool.load(context, R.raw.reset, 0);
    }

    @Override
    public void playMove() {
        soundPool.stop(Move);
        soundPool.play(Move, 50f, 50f, 0, 0, 0);
    }

    @Override
    public void stopMoving() {
        soundPool.stop(Move);
    }

    @Override
    public void playTargetCollected() {
        soundPool.play(TargetCollected, 5f, 5f, 0, 0, 0);
    }

    @Override
    public void playAllTargetsCollected() {
        soundPool.play(AllTargetsCollected, 5f, 5f, 0, 0, 0);
    }

    @Override
    public void playTouchWall() {
        soundPool.play(TouchWall, 80f, 80f, 10, 0, 0);
    }

    @Override
    public void playReset() {
        soundPool.stop(Reset);
        soundPool.play(Reset, 5f, 5f, 0, 0, 0);
    }
}
