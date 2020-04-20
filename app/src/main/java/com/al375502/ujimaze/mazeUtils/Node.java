package com.al375502.ujimaze.mazeUtils;

public class Node {
    private int peso;
    private Position position;
    private boolean Known;
    private Node Path;

        public Node(int peso, Position position, Node p) {
        this.peso = peso;
        this.position = position;
        Known = false;
        Path = p;
    }

    public int getPeso() {
        return peso;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isKnown() {
        return Known;
    }

    public Node getPath() {
        return Path;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public void setKnown(boolean known) {
        Known = known;
    }

    public void setPath(Node path) {
        Path = path;
    }
}
