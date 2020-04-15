package com.al375502.ujimaze.mazeUtils;

import java.util.ArrayList;

public class Node {
    public ArrayList<Node> Hijo = new ArrayList<>();
    public ArrayList<Integer> arcos = new ArrayList<>();
    public int peso;
    public int X;
    public int Y;
    public boolean Known;
    public Node Path;

    
    public int lenght(ArrayList<Node> aux, int i, int u){
        return aux.get(i).arcos.get(u);
    }
}
