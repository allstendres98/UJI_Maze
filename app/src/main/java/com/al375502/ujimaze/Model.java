package com.al375502.ujimaze;

import android.content.Context;
import android.util.Log;
import android.util.Range;

import com.al375502.ujimaze.mazeUtils.Direction;
import com.al375502.ujimaze.mazeUtils.Maze;
import com.al375502.ujimaze.mazeUtils.Position;

import java.util.Stack;

public class Model {

public boolean playerIsMoving = false;
public float playerCurrentPositionX;
public float playerCurrentPositionY;
public Position playerCurrentPosition;
public Position playerNextPosition = new Position(0,0);
private Stack<Position> playerPreviousPosition = new Stack<>();

private int currentMazeIndex = 0;
private int speed = 400;
private float[] cellX;
private float[] cellY;

    public Model(float[] cellX, float[] cellY) {
        this.cellX = cellX;
        this.cellY = cellY;
        playerCurrentPosition = new Position(Levels.mazes[getCurrentMaze()].getOrigin());
        playerCurrentPositionX = cellX[playerCurrentPosition.getCol()];
        playerCurrentPositionY = cellY[playerCurrentPosition.getRow()];
    }

public void resetMaze(){
    playerCurrentPosition = new Position(Levels.mazes[getCurrentMaze()].getOrigin());
    playerCurrentPositionX = cellX[playerCurrentPosition.getCol()];
    playerCurrentPositionY = cellY[playerCurrentPosition.getRow()];
    while(!playerPreviousPosition.empty()) playerPreviousPosition.pop();
    //resetear targets
}
public void goToPreviousPosition(){
        if(!playerPreviousPosition.empty())
        {
            playerCurrentPosition = playerPreviousPosition.pop();
            playerCurrentPositionX = cellX[playerCurrentPosition.getCol()];
            playerCurrentPositionY = cellY[playerCurrentPosition.getRow()];
            Log.d("cell","x: " + playerCurrentPosition.getCol() + " y: " + playerCurrentPosition.getRow());
        }
}
public int getCurrentMaze(){ return currentMazeIndex; }
public void moveNextMaze(){ currentMazeIndex++; }
public void getTargetPosition(){}

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
    }
}
public void playerReachTarget(){}

public void calculateNextPosition(Direction direction) {
        if(Levels.mazes[getCurrentMaze()].hasWall(playerCurrentPosition,direction)) return;
        else {
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
