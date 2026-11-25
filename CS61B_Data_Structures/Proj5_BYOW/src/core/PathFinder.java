package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class PathFinder {
    private static final int[][] DIRECTIONS = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1}
    };

    public static List<Position> shortestPath(TETile[][] world, int width, int height,
                                              int startX, int startY,
                                              int targetX, int targetY) {
        if (!inBounds(targetX, targetY, width, height)) {
            return null;
        }

        if (!isTraversable(world, targetX, targetY, targetX, targetY)) {
            return null;
        }

        boolean[][] visited = new boolean[width][height];
        Position[][] parent = new Position[width][height];
        ArrayDeque<Position> fringe = new ArrayDeque<>();

        Position start = new Position(startX, startY);
        fringe.add(start);
        visited[startX][startY] = true;

        while (!fringe.isEmpty()) {
            Position current = fringe.removeFirst();

            if (current.x == targetX && current.y == targetY) {
                return reconstructPath(parent, current);
            }

            for (int[] direction : DIRECTIONS) {
                int nextX = current.x + direction[0];
                int nextY = current.y + direction[1];

                if (!inBounds(nextX, nextY, width, height)) {
                    continue;
                }

                if (visited[nextX][nextY]) {
                    continue;
                }

                if (!isTraversable(world, nextX, nextY, targetX, targetY)) {
                    continue;
                }

                visited[nextX][nextY] = true;
                parent[nextX][nextY] = current;
                fringe.addLast(new Position(nextX, nextY));
            }
        }

        return null;
    }

    private static boolean inBounds(int x, int y, int width, int height) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    private static boolean isTraversable(TETile[][] world, int x, int y,
                                         int targetX, int targetY) {
        TETile tile = world[x][y];

        if (tile == Tileset.FLOOR || tile == Tileset.AVATAR || tile == Tileset.SAND) {
            return true;
        }

        if (tile == Tileset.COIN) {
            return true;
        }

        return tile == Tileset.LOCKED_DOOR && x == targetX && y == targetY;
    }

    private static List<Position> reconstructPath(Position[][] parent, Position target) {
        ArrayList<Position> reversed = new ArrayList<>();

        Position current = target;
        while (current != null) {
            reversed.add(current);
            current = parent[current.x][current.y];
        }

        ArrayList<Position> path = new ArrayList<>();
        for (int i = reversed.size() - 1; i >= 0; i--) {
            path.add(reversed.get(i));
        }

        return path;
    }
}