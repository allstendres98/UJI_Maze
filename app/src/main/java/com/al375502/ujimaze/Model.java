package com.al375502.ujimaze;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.util.Range;

import com.al375502.ujimaze.mazeUtils.Direction;
import com.al375502.ujimaze.mazeUtils.Maze;
import com.al375502.ujimaze.mazeUtils.Position;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class Model {

public boolean playerIsMoving = false;
public float playerCurrentPositionX;
public float playerCurrentPositionY;
public Position playerCurrentPosition;
public Position playerNextPosition = new Position(0,0);
public Position[] targets;
public int numTargets;
public boolean[] targetsCollected;
private Stack<Position> playerPreviousPosition = new Stack<>();
public int movementsCount = 0;
public Integer[] targetsCollectedMovement;

private int currentMazeIndex = 0;
private int speed = 200;
private float[] cellX;
private float[] cellY;

    public Model(float[] cellX, float[] cellY) {
        this.cellX = cellX;
        this.cellY = cellY;
        playerCurrentPosition = new Position(Levels.mazes[getCurrentMaze()].getOrigin());
        playerCurrentPositionX = cellX[playerCurrentPosition.getCol()];
        playerCurrentPositionY = cellY[playerCurrentPosition.getRow()];
        targets = Levels.mazes[getCurrentMaze()].getTargets().toArray(new Position[0]);
        numTargets = targets.length;
        targetsCollected = new boolean[numTargets];
        for(int i=0;i<numTargets;i++) targetsCollected[i]=false;
        targetsCollectedMovement = new Integer[numTargets];
    }

public void resetMaze(){
        playerIsMoving=false;
    playerCurrentPosition = new Position(Levels.mazes[getCurrentMaze()].getOrigin());
    playerCurrentPositionX = cellX[playerCurrentPosition.getCol()];
    playerCurrentPositionY = cellY[playerCurrentPosition.getRow()];
    while(!playerPreviousPosition.empty()) playerPreviousPosition.pop();
    movementsCount = 0;
    targets = Levels.mazes[getCurrentMaze()].getTargets().toArray(new Position[0]);
    numTargets = targets.length;
    targetsCollectedMovement = new Integer[numTargets];
    targetsCollected = new boolean[numTargets];
    for(int i=0;i<numTargets;i++) targetsCollected[i]=false;
}
public void goToPreviousPosition(){
        if(!playerPreviousPosition.empty())
        {
           for(int i = 0; i<targetsCollectedMovement.length;i++)
                {
                    if(targetsCollectedMovement[i]!= null &&targetsCollectedMovement[i] == movementsCount) {
                        numTargets++;
                        targetsCollected[i] = false;
                        targetsCollectedMovement[i] = -1;
                    }
                }
            movementsCount--;
            playerCurrentPosition = playerPreviousPosition.pop();
            playerCurrentPositionX = cellX[playerCurrentPosition.getCol()];
            playerCurrentPositionY = cellY[playerCurrentPosition.getRow()];
            Log.d("cell","x: " + playerCurrentPosition.getCol() + " y: " + playerCurrentPosition.getRow());
        }
}
public int getCurrentMaze(){ return currentMazeIndex; }
public void moveNextMaze(){ currentMazeIndex++; resetMaze();}
public void getTargetPosition(){

}

public void startMovingDirection(Direction direction, float deltaTime){
    if(playerIsMoving) {

        Range<Integer> x = new Range<Integer>(Math.round(playerCurrentPositionX)-10, Math.round(playerCurrentPositionX)+10);
        Range<Integer> y = new Range<Integer>(Math.round(playerCurrentPositionY)-10, Math.round(playerCurrentPositionY)+10);

        if ( x.contains(Math.round(cellX[playerNextPosition.getCol()])) && y.contains(Math.round(cellY[playerNextPosition.getRow()]))) {
            playerCurrentPositionX = cellX[playerNextPosition.getCol()];
            playerCurrentPositionY = cellY[playerNextPosition.getRow()];
            playerCurrentPosition.setCol(playerNextPosition.getCol());
            playerCurrentPosition.setRow(playerNextPosition.getRow());
            playerIsMoving = false;
        }
        else {
            playerCurrentPositionX += direction.getCol() * deltaTime * speed;
            playerCurrentPositionY += direction.getRow() * deltaTime * speed;
        }

        Range<Integer> x1 = new Range<Integer>(Math.round(playerCurrentPositionX)-7, Math.round(playerCurrentPositionX)+7);
        Range<Integer> y1 = new Range<Integer>(Math.round(playerCurrentPositionY)-7, Math.round(playerCurrentPositionY)+7);
        for(int i = 0; i < targets.length; i++)
        {
            if(x1.contains(Math.round(cellX[targets[i].getCol()])) && y1.contains(Math.round(cellY[targets[i].getRow()])))
            {
                if(targetsCollected[i] == false) {
                    targetsCollectedMovement[i] = movementsCount;
                    targetsCollected[i] = true;
                    numTargets--;
                    if(numTargets == 0) moveNextMaze();
                }
            }
        }
    }
}

public void playerReachTarget(){}

public void calculateNextPosition(Direction direction) {
        if(Levels.mazes[getCurrentMaze()].hasWall(playerCurrentPosition,direction)) return;
        else {
            movementsCount++;
            playerPreviousPosition.push(new Position(playerCurrentPosition.getRow(), playerCurrentPosition.getCol()));
            while (!Levels.mazes[getCurrentMaze()].hasWall(playerCurrentPosition, direction) && !playerIsMoving) {
                playerNextPosition.setCol(playerCurrentPosition.getCol() + direction.getCol());
                playerNextPosition.setRow(playerCurrentPosition.getRow() + direction.getRow());
                playerCurrentPosition = playerNextPosition;
            }
            playerIsMoving = true;
        }
    }
}
