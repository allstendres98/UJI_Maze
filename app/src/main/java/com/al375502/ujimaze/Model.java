package com.al375502.ujimaze;

import android.content.Context;

import com.al375502.ujimaze.mazeUtils.Maze;
import com.al375502.ujimaze.mazeUtils.Position;

public class Model {

public boolean playerIsMoving;

public void resetMaze(){}
public void getCurrentMaze(){}
public void moveNextMaze(){}
public void getTargetPosition(){}
public void getPlayerCoordinates(){}
public void startMovingDirection(){
    if(playerIsMoving) return;
}
public void playerReachTarget(){}
}
