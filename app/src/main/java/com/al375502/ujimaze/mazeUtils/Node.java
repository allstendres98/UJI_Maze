package com.al375502.ujimaze.mazeUtils;

import java.util.ArrayList;

public class Node {
    //public ArrayList<Node> Hijo = new ArrayList<>();
    public int peso;
    public Position position;
    public boolean Known;
    public Node Path;

        public Node(int peso, Position position, Node p) {
        this.peso = peso;
        this.position = position;
        Known = false;
        Path = p;
    }
}
