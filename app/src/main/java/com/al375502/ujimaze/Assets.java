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

    public static Bitmap playerUp;
    public static Bitmap playerDown;
    public static Bitmap playerLeft;
    public static Bitmap playerRight;

    public static Bitmap target0;
    public static Bitmap target1;
    public static Bitmap target2;
    public static Bitmap target3;

    public static Drawable reset;
    public static Drawable undo;
    public static Drawable help;

    public static Bitmap wallsSprites;
    public static SpriteSheet walls;
    public static Bitmap[] wallsBitmaps = new Bitmap[15];

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

        if(playerUp != null) playerUp.recycle();
        if(playerDown != null) playerDown.recycle();
        if(playerLeft != null) playerLeft.recycle();
        if(playerRight != null) playerRight.recycle();

        playerUp = players.getScaledSprite(3, 0, playerSide, playerSide);
        playerDown = players.getScaledSprite(0, 0, playerSide, playerSide);
        playerLeft = players.getScaledSprite(1, 0, playerSide, playerSide);
        playerRight = players.getScaledSprite(2, 0, playerSide, playerSide);

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

        target0 = targets.getScaledSprite(0, 0, playerSide, playerSide);
        target1 = targets.getScaledSprite(0, 1, playerSide, playerSide);
        target2 = targets.getScaledSprite(0, 2, playerSide, playerSide);
        target3 = targets.getScaledSprite(0, 3, playerSide, playerSide);
    }
}
