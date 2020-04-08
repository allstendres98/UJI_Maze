package com.al375502.ujimaze.mazeUtils;

/**
 * An enumeration for representing directions in a {@link Maze}.
 *
 * @author Juan Miguel Vilar Torres (jvilar@uji.es)
 */
public enum Direction {
    UP(-1, 0),
    DOWN(1,0),
    LEFT(0, -1),
    RIGHT(0, 1);

    private final int row, col;

    Direction(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * The value that must be added to a row to move in this direction.
     * @return The value.
     */
    public int getRow() {
        return row;
    }

    /**
     * The value that must be added to a column to move in this direction.
     * @return The value.
     */
    public int getCol() {
        return col;
    }
}
