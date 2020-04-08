package com.al375502.ujimaze.mazeUtils;

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * A class to represent a position in a {@link Maze}. A position is
 * defined by a row and a column.
 *
 * @author Juan Miguel Vilar Torres (jvilar@uji.es)
 */
public class Position {
    private int row, col;

    /**
     * The constructor.
     * @param row the row.
     * @param col the column.
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * A copy constructor.
     * @param position the other position.
     */
    public Position(Position position) {
        this(position.row, position.col);
    }

    /**
     * A getter for the row.
     * @return The row.
     */
    public int getRow() {
        return row;
    }

    /**
     * A getter for the column.
     * @return The column.
     */
    public int getCol() {
        return col;
    }

    /**
     * A setter for the row.
     * @param row The row.
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * A setter for the column.
     * @param col The column.
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * Copy the other {@link Position} into this one.
     * @param other the other.
     */
    public void set(Position other) {
        row = other.row;
        col = other.col;
    }

    /**
     * Simultaneously set the row and the column.
     * @param row the row.
     * @param col the column.
     */
    public void set(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row &&
                col == position.col;
    }

    /**
     * Check whether the position is equal to the given row and column.
     * @param row the row.
     * @param col the column.
     * @return {@code true} if the position is equal to {@code (row, col)}.
     */
    public boolean equals(int row, int col) {
        return this.row == row &&
                this.col == col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    /**
     * Move in the given {@link Direction}.
     * @param direction
     */
    public void move(Direction direction) {
        row += direction.getRow();
        col += direction.getCol();
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("(%d, %d)", row, col);
    }
}
