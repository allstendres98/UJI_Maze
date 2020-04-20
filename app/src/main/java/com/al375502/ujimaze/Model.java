package com.al375502.ujimaze;

import android.media.MediaPlayer;
import android.util.Log;
import android.util.Range;

import com.al375502.ujimaze.mazeUtils.Direction;
import com.al375502.ujimaze.mazeUtils.Node;
import com.al375502.ujimaze.mazeUtils.Position;

import java.util.ArrayList;
import java.util.Stack;
public class Model {

    public boolean gameOver = false;
    public boolean playerIsMoving = false;
    public float playerCurrentPositionX;
    public float playerCurrentPositionY;
    public Position playerCurrentPosition;
    public Position playerNextPosition = new Position(0,0);
    public Position[] targets;
    public int numTargets;
    public boolean[] targetsCollected;
    public int movementsCount = 0;
    public Integer[] targetsCollectedMovement;
    public Position[] enemies;
    public Direction[] enemieDirectionToGo;
    public Position[] enemieCurrentPosition;
    public float[] enemieX, enemieY;
    public Position[] positionToChange;
    public boolean[] isChanging;
    public int currentMazeIndex = 0;
    public ArrayList<Node> Nodes = new ArrayList<>();
    public ArrayList<Node> targetsNodes = new ArrayList<>();

    private int speed = 200;
    private float[] cellX;
    private float[] cellY;
    private Stack<Position> playerPreviousPosition = new Stack<>();
    private SoundPlayer soundPlayer;

    public interface SoundPlayer{
        void playMove();
        void stopMoving();
        void playTargetCollected();
        void playAllTargetsCollected();
        void playTouchWall();
        void playReset();
    }

    public Model(float[] cellX, float[] cellY, SoundPlayer soundPlayer, MediaPlayer mediaPlayer) {
        this.cellX = cellX;
        this.cellY = cellY;
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
        this.soundPlayer = soundPlayer;
        mediaPlayer.start();
        enemies = Levels.mazes[getCurrentMaze()].getEnemies().toArray(new Position[0]);
        enemieDirectionToGo = new Direction[enemies.length];
        enemieCurrentPosition = new Position[enemies.length];
        enemieY = new float[enemies.length];
        enemieX = new float[enemies.length];
        isChanging = new boolean[enemies.length];
        for(int i = 0; i < enemies.length; i++){
            enemieCurrentPosition[i] = enemies[i];
            enemieX[i] = cellX[enemieCurrentPosition[i].getCol()];
            enemieY[i] = cellY[enemieCurrentPosition[i].getRow()];
            defineEnemieDirection(i);
            isChanging[i] = true;
        }
        positionToChange = enemieCurrentPosition;
        Dijsktra();
    }

    public void touchResetButton()
    {
        resetMaze();
        soundPlayer.playReset();
    }

    public void Dijsktra() {
        Nodes.clear();
        targetsNodes.clear();
        boolean alltargetsreached = false;
        Nodes.add(new Node(0,playerCurrentPosition,null)); //Node origin
        Direction[] directions = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
        while(!alltargetsreached)
        {
            Node actualNode = new Node(100,new Position(-1,-1),null); //Un nodo que siempre va a tener un peso mayor al de todos los demas como auxiliar
            int x = 0;
            for (Node n:Nodes) {
                if(!n.isKnown() && actualNode.getPeso() > n.getPeso()) actualNode = n;  //si no conozco el camino optimo para llegar a un nodo y su peso es el menor de todos ya conozco su camino optimo
                else x++;
            }
            actualNode.setKnown(true);
            if(Nodes.size()  == x) alltargetsreached = true;  //si los he visto todos paro
            for (Position t:targets) { //comparo si el nodo que he hayado corresponde a un target y lo añado a un array aparte con el que los dibujare
                if(t.equals(actualNode.getPosition())) {
                    targetsNodes.add(actualNode);
                }
            }
            if (!alltargetsreached){
                int cont;
                Position aux;
                for(int i = 0; i < 4; i++)
                {
                    cont = 0;
                    aux = new Position(actualNode.getPosition());
                    while(!Levels.mazes[getCurrentMaze()].hasWall(aux, directions[i]))
                    {
                        cont++;
                        aux.move(directions[i]);
                    }
                    boolean add = true;
                    if(!aux.equals(actualNode.getPosition())){ // si no me he movido no me añado
                        for (Node n :Nodes) {
                            if (aux.equals(n.getPosition())) { // si me he movido pero ya conocia el nodo destino, cambio su peso si mi camino es mas optimo
                                add = false;
                                if (actualNode.getPeso() + cont < n.getPeso()) { // da igual que vuelva a mi nodo padre, osea que retroceda porque comprobara que el camino que me ha costado llegar es mayor asique no modificara anda
                                    n.setPath(actualNode);
                                    n.setPeso(actualNode.getPeso() + cont);
                                }
                            }
                        }
                        if(add){
                            Nodes.add(new Node(actualNode.getPeso() + cont, aux, actualNode)); //si no me conocian me añado
                        }
                    }
                }
            }
        }
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
    Dijsktra();
    }

    private void defineEnemieDirection(int i) {
        if(currentMazeIndex == 3 || currentMazeIndex == 1) {
            enemieDirectionToGo[i] = Direction.UP;
        }
        else enemieDirectionToGo[i] = Direction.RIGHT;
    }

    public void goToPreviousPosition(){
        if(!playerPreviousPosition.empty())
        {
           for(int i = 0; i<targetsCollectedMovement.length;i++)
                {
                    if(targetsCollectedMovement[i]!= null && targetsCollectedMovement[i] == movementsCount) {
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

    public void moveNextMaze() {
        if (getCurrentMaze() < Levels.mazes.length - 1) {
            soundPlayer.playAllTargetsCollected();
            currentMazeIndex++;
            resetMaze();
            enemies = Levels.mazes[getCurrentMaze()].getEnemies().toArray(new Position[0]);
            enemieDirectionToGo = new Direction[enemies.length];
            enemieCurrentPosition = new Position[enemies.length];
            enemieY = new float[enemies.length];
            enemieX = new float[enemies.length];
            isChanging = new boolean[enemies.length];
            for (int i = 0; i < enemies.length; i++) {
                enemieCurrentPosition[i] = enemies[i];
                enemieX[i] = cellX[enemieCurrentPosition[i].getCol()];
                enemieY[i] = cellY[enemieCurrentPosition[i].getRow()];
                defineEnemieDirection(i);
                isChanging[i] = true;
            }
            positionToChange = enemieCurrentPosition;
        }
        else{
            gameOver = true;
        }
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
                soundPlayer.playTouchWall();
                soundPlayer.stopMoving();
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
                        if(numTargets != 1) soundPlayer.playTargetCollected();
                        numTargets--;
                        if(numTargets == 0) moveNextMaze();
                    }
                }
            }
        }
    }

    public void calculateNextPosition(Direction direction) {
            if(Levels.mazes[getCurrentMaze()].hasWall(playerCurrentPosition,direction)) {soundPlayer.playTouchWall(); return;}
            else {
                soundPlayer.playMove();
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


    public void moveEnemies(float deltaTime)
    {
       for(int i = 0; i < enemies.length; i++)
           {
               while (!Levels.mazes[getCurrentMaze()].hasWall(enemieCurrentPosition[i], enemieDirectionToGo[i]) && isChanging[i])
               {
                   positionToChange[i].setCol(enemieCurrentPosition[i].getCol() + enemieDirectionToGo[i].getCol());
                   positionToChange[i].setRow(enemieCurrentPosition[i].getRow() + enemieDirectionToGo[i].getRow());
                   enemieCurrentPosition[i] = positionToChange[i];
               }

               isChanging[i] = false;

                   enemieX[i] += enemieDirectionToGo[i].getCol() * deltaTime * 100;
                   enemieY[i] += enemieDirectionToGo[i].getRow() * deltaTime * 100;

               Range<Integer> x = new Range<Integer>(Math.round(enemieX[i])-10, Math.round(enemieX[i])+10);
               Range<Integer> y = new Range<Integer>(Math.round(enemieY[i])-10, Math.round(enemieY[i])+10);

               if(enemieDirectionToGo[i] == Direction.UP)
               {
                   if ( x.contains(Math.round(cellX[enemieCurrentPosition[i].getCol()])) && y.contains(Math.round(cellY[enemieCurrentPosition[i].getRow()]))) {
                       isChanging[i] = true;
                       enemieDirectionToGo[i] = Direction.DOWN;
                       enemieX[i] = cellX[enemieCurrentPosition[i].getCol()];
                       enemieY[i] = cellY[enemieCurrentPosition[i].getRow()];
                   }
               }
               else if(enemieDirectionToGo[i] == Direction.DOWN)
               {
                   if ( x.contains(Math.round(cellX[enemieCurrentPosition[i].getCol()])) && y.contains(Math.round(cellY[enemieCurrentPosition[i].getRow()]))) {
                       isChanging[i] = true;
                       enemieDirectionToGo[i] = Direction.UP;
                       enemieX[i] = cellX[enemieCurrentPosition[i].getCol()];
                       enemieY[i] = cellY[enemieCurrentPosition[i].getRow()];
                   }
               }
               else if(enemieDirectionToGo[i] == Direction.LEFT)
               {
                   if ( x.contains(Math.round(cellX[enemieCurrentPosition[i].getCol()])) && y.contains(Math.round(cellY[enemieCurrentPosition[i].getRow()]))) {
                       isChanging[i] = true;
                       enemieDirectionToGo[i] = Direction.RIGHT;
                       enemieX[i] = cellX[enemieCurrentPosition[i].getCol()];
                       enemieY[i] = cellY[enemieCurrentPosition[i].getRow()];
                   }
               }
               else if(enemieDirectionToGo[i] == Direction.RIGHT)
               {
                   if ( x.contains(Math.round(cellX[enemieCurrentPosition[i].getCol()])) && y.contains(Math.round(cellY[enemieCurrentPosition[i].getRow()]))) {
                       isChanging[i] = true;
                       enemieDirectionToGo[i] = Direction.LEFT;
                       enemieX[i] = cellX[enemieCurrentPosition[i].getCol()];
                       enemieY[i] = cellY[enemieCurrentPosition[i].getRow()];
                   }
               }
               Range<Integer> x1 = new Range<Integer>(Math.round(enemieX[i])-40, Math.round(enemieX[i])+40);
               Range<Integer> y1 = new Range<Integer>(Math.round(enemieY[i])-40, Math.round(enemieY[i])+40);
               if ( x1.contains(Math.round(playerCurrentPositionX)) && y1.contains(Math.round(playerCurrentPositionY))) {
                   resetMaze();
               }

           }
        }
}

