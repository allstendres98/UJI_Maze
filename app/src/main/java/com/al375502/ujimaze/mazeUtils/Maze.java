package com.al375502.ujimaze.mazeUtils;

import androidx.annotation.NonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.al375502.ujimaze.mazeUtils.Direction.DOWN;
import static com.al375502.ujimaze.mazeUtils.Direction.LEFT;
import static com.al375502.ujimaze.mazeUtils.Direction.RIGHT;
import static com.al375502.ujimaze.mazeUtils.Direction.UP;

/**
 * A class for representing mazes.
 *
 * @author Juan Miguel Vilar Torres (jvilar@uji.es)
 */
public class Maze {
    /**
     * The {@code char} used for representing the origin.
     */
    public static final char ORIGIN = 'O';
    /**
     * The {@code char} used for representing the targets.
     */
    public static final char TARGET = 'X';

    public static final char ENEMY = 'E';

    private final boolean walls[][][];
    private final Position origin;
    private final Set<Position> targets;
    private final Set<Position> enemies;

    /**
     * <p>The constructor.</p>
     *
     * <p>The parameter to the constructor is an array of {@link String} with
     * an string for each of the rows. The characters {@link Maze#ORIGIN} and
     * {@link Maze#TARGET} are used to represent the origin and the targets,
     * respectively. Nonempty characters are used to represent the walls. An
     * example maze follows:</p>
     * <pre>
     * {
     *     "+-+-+-+-+-+-+-+",
     *     "| |     |     |",
     *     "+ + +-+ +   +-+",
     *     "|        O    |",
     *     "+     +     + +",
     *     "|     |     | |",
     *     "+ +-+ + + + + +",
     *     "|       | |   |",
     *     "+      -+ +  -+",
     *     "|             |",
     *     "+-+ + + +-+   +",
     *     "|   |X|       |",
     *     "+   +-+ +   + +",
     *     "|       |   | |",
     *     "+-+-+-+-+-+-+-+"
     * }
     * </pre>
     * @param diagram a schematic representation of the maze.
     */
    public Maze(String[] diagram) {
        int nrows = (diagram.length - 1)/2;
        int ncols = (diagram[0].length() - 1)/2;

        walls = new boolean[nrows][ncols][4];

        Position origin = null;

        targets = new HashSet<>();
        enemies = new HashSet<>();

        for (int row = 0 ; row < nrows ; row++) {
            String previous = diagram[2*row];
            String current = diagram[2*row + 1];
            String next = diagram[2*row + 2];
            for (int col = 0 ; col < ncols ; col++) {
                int realCol = 2 * col + 1;
                if (current.charAt(realCol) == ORIGIN)
                    origin = new Position(row, col);
                if (current.charAt(realCol) == TARGET)
                    targets.add(new Position(row, col));
                if (current.charAt(realCol) == ENEMY)
                    enemies.add(new Position(row, col));
                walls[row][col][UP.ordinal()] = previous.charAt(realCol) != ' ';
                walls[row][col][DOWN.ordinal()] = next.charAt(realCol) != ' ';
                walls[row][col][LEFT.ordinal()] = current.charAt(realCol - 1) != ' ';
                walls[row][col][RIGHT.ordinal()] = current.charAt(realCol + 1) != ' ';
            }
        }

        this.origin = origin;
    }

    /**
     * Getter the {@link Position} of the origin.
     * @return The position of the origin.
     */
    public Position getOrigin() {
        return origin;
    }

    /**
     * Getter for the positions of the targets of the maze.
     * @return The positions of the targets.
     */
    public Collection<Position> getTargets() {
        return targets;
    }
    public Collection<Position> getEnemies() {
        return enemies;
    }

    /**
     * Getter for the number of rows of the maze.
     *
     * @return The number of rows of the maze.
     */
    public int getNRows() {
        return walls.length;
    }

    /**
     * Getter for the number of columns of the maze.
     *
     * @return The number of columns of the maze.
     */
    public int getNCols() {
        return walls[0].length;
    }


    @NonNull
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Position position = new Position(0, 0);
        for (int col = 0 ; col < getNCols() ; col++) {
            position.setCol(col);
            if ( hasWall(0, col-1, UP) ||
                    hasWall(position, LEFT) ||
                    hasWall(position, UP)
            )
                builder.append('+');
            else
                builder.append(' ');
            builder.append(hasWall(position, UP) ? '-' : ' ');
        }
        if (hasWall(position, UP) || hasWall(position, RIGHT))
            builder.append("+\n");
        else
            builder.append(" \n");
        for (int row = 0 ; row < getNRows() ; row++) {
            position.setRow(row);
            for (int col = 0; col < getNCols() ; col++) {
                position.setCol(col);
                builder.append(hasWall(position, LEFT) ? '|' : ' ');
                builder.append(origin.equals(position) ? ORIGIN :
                        targets.contains(position) ? TARGET :
                        enemies.contains(position) ? ENEMY :
                        ' '
                );
            }
            builder.append(hasWall(position, RIGHT) ? '|' : ' ');
            builder.append('\n');
            for (int col = 0 ; col < getNCols() ; col++) {
                position.setCol(col);
                if ( hasWall(row, col-1, DOWN) ||
                        hasWall(position, LEFT) ||
                        hasWall(position, DOWN) ||
                        hasWall(row+1, col, LEFT)
                )
                    builder.append('+');
                else
                    builder.append(' ');
                builder.append(hasWall(position, DOWN) ? '-' : ' ');
            }
            if (hasWall(position, DOWN) || hasWall(position, RIGHT))
                builder.append("+\n");
            else
                builder.append(" \n");
        }
        return builder.toString();
    }

    /**
     * Check for the existence of a wall in the given {@link Direction} in
     * a position determined by a row and a column.
     * @param row the row.
     * @param col the column.
     * @param direction the direction.
     * @return {@code true} if there is a wall.
     */
    public boolean hasWall(int row, int col, Direction direction) {
        return 0 <= row && row < walls.length &&
                0 <= col && col < walls[0].length &&
                walls[row][col][direction.ordinal()];
    }

    /**
     * Check for the existence of a wall in a given {@link Position} and {@link Direction}.
     * @param position the position
     * @param direction the direction
     * @return {@code true} if there is a wall.
     */
    public boolean hasWall(Position position, Direction direction) {
        return hasWall(position.getRow(), position.getCol(), direction);
    }
}
