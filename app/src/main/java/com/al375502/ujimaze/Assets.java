package com.al375502.ujimaze;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import es.uji.vj1229.framework.SpriteSheet;

public class Assets {
    private static final int PLAYER_SIDE = 64;

    private static Bitmap playerSprites;
    private static SpriteSheet players;

    private static Bitmap targetSprites;
    private static SpriteSheet targets;

    public static Bitmap playerUp0;
    public static Bitmap playerDown0;
    public static Bitmap playerLeft0;
    public static Bitmap playerRight0;

    public static Bitmap playerUp1;
    public static Bitmap playerDown1;
    public static Bitmap playerLeft1;
    public static Bitmap playerRight1;

    public static Bitmap playerUp2;
    public static Bitmap playerDown2;
    public static Bitmap playerLeft2;
    public static Bitmap playerRight2;

    public static Bitmap playerUp3;
    public static Bitmap playerDown3;
    public static Bitmap playerLeft3;
    public static Bitmap playerRight3;

    public static Bitmap target0;
    public static Bitmap target1;
    public static Bitmap target2;
    public static Bitmap target3;
    public static Bitmap target4;
    public static Bitmap target5;

    public static Drawable reset;
    public static Drawable undo;
    public static Drawable help;

    public static Bitmap wallsSprites;
    public static SpriteSheet walls;
    public static Bitmap[] wallsBitmaps = new Bitmap[15];

    public static Bitmap grassSprites;
    public static SpriteSheet grassSpriteSheet;
    public static Bitmap grass;

    public static void createWallsAssets(Context context, int playerSide)
    {
        Resources resources = context.getResources();
        //Define player variables
        if(wallsSprites != null) wallsSprites.recycle();
        wallsSprites = BitmapFactory.decodeResource(resources,R.drawable.walls);
        walls = new SpriteSheet(wallsSprites, 32, 32);

        for(int i = 0; i < wallsBitmaps.length; i++)
        {
            if(wallsBitmaps[i] != null) wallsBitmaps[i].recycle();
            wallsBitmaps[i] = walls.getScaledSprite(0, i, playerSide, playerSide);
        }
    }

    public static void createPlayerAssets(Context context, int playerSide)
    {
        Resources resources = context.getResources();
        //Define player variables
        if(playerSprites != null) playerSprites.recycle();
        playerSprites = BitmapFactory.decodeResource(resources,R.drawable.player);
        players = new SpriteSheet(playerSprites, PLAYER_SIDE, PLAYER_SIDE);

        if(playerUp0 != null) playerUp0.recycle();
        if(playerDown0 != null) playerDown0.recycle();
        if(playerLeft0 != null) playerLeft0.recycle();
        if(playerRight0 != null) playerRight0.recycle();

        if(playerUp1 != null) playerUp1.recycle();
        if(playerDown1 != null) playerDown1.recycle();
        if(playerLeft1 != null) playerLeft1.recycle();
        if(playerRight1 != null) playerRight1.recycle();

        if(playerUp2 != null) playerUp2.recycle();
        if(playerDown2 != null) playerDown2.recycle();
        if(playerLeft2 != null) playerLeft2.recycle();
        if(playerRight2 != null) playerRight2.recycle();

        if(playerUp3 != null) playerUp3.recycle();
        if(playerDown3 != null) playerDown3.recycle();
        if(playerLeft3 != null) playerLeft3.recycle();
        if(playerRight3 != null) playerRight3.recycle();

        playerUp0 = players.getScaledSprite(3, 0, playerSide, playerSide);
        playerDown0 = players.getScaledSprite(0, 0, playerSide, playerSide);
        playerLeft0 = players.getScaledSprite(1, 0, playerSide, playerSide);
        playerRight0 = players.getScaledSprite(2, 0, playerSide, playerSide);

        playerUp1 = players.getScaledSprite(3, 1, playerSide, playerSide);
        playerDown1 = players.getScaledSprite(0, 1, playerSide, playerSide);
        playerLeft1 = players.getScaledSprite(1, 1, playerSide, playerSide);
        playerRight1 = players.getScaledSprite(2, 1, playerSide, playerSide);

        playerUp2 = players.getScaledSprite(3, 2, playerSide, playerSide);
        playerDown2 = players.getScaledSprite(0, 2, playerSide, playerSide);
        playerLeft2 = players.getScaledSprite(1, 2, playerSide, playerSide);
        playerRight2 = players.getScaledSprite(2, 2, playerSide, playerSide);

        playerUp3 = players.getScaledSprite(3, 3, playerSide, playerSide);
        playerDown3 = players.getScaledSprite(0, 3, playerSide, playerSide);
        playerLeft3 = players.getScaledSprite(1, 3, playerSide, playerSide);
        playerRight3 = players.getScaledSprite(2, 3, playerSide, playerSide);

        //Define buttons variables
        if(reset == null) reset = context.getDrawable(R.drawable.reset);
        if(undo == null) undo = context.getDrawable(R.drawable.undo);
        if(help == null) help = context.getDrawable(R.drawable.help);
    }

    public static void createTargetAssets(Context context, int playerSide)
    {
        Resources resources = context.getResources();
        //Define target variables
        if(targetSprites != null) targetSprites.recycle();
        targetSprites = BitmapFactory.decodeResource(resources,R.drawable.target);
        targets = new SpriteSheet(targetSprites, PLAYER_SIDE, PLAYER_SIDE);

        if(target0 != null) target0.recycle();
        if(target1 != null) target1.recycle();
        if(target2 != null) target2.recycle();
        if(target3 != null) target3.recycle();
        if(target4 != null) target4.recycle();
        if(target5 != null) target5.recycle();

        target0 = targets.getScaledSprite(0, 0, playerSide, playerSide);
        target1 = targets.getScaledSprite(0, 1, playerSide, playerSide);
        target2 = targets.getScaledSprite(0, 2, playerSide, playerSide);
        target3 = targets.getScaledSprite(0, 3, playerSide, playerSide);
        target4 = targets.getScaledSprite(0, 4, playerSide, playerSide);
        target5 = targets.getScaledSprite(0, 5, playerSide, playerSide);
    }

    public static void createGrassAssets(Context context, int playerSide, int lineWidth)
    {
        Resources resources = context.getResources();
        if(grassSprites != null) grassSprites.recycle();
        grassSprites = BitmapFactory.decodeResource(resources,R.drawable.grass);
        grassSpriteSheet = new SpriteSheet(grassSprites, 512, 512);

        if(grass != null) grass.recycle();

        grass = grassSpriteSheet.getScaledSprite(0,0,playerSide+lineWidth,playerSide+lineWidth);
    }
}
