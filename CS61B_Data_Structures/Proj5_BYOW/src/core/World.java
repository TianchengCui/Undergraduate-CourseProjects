package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;

public class World {
    private final int width;
    private final int height;
    private final long seed;
    private final Random random;
    private final TETile[][] world;
    private final LinkedHashSet<Room> rooms;
    private final boolean[][] roomInterior;
    private final boolean[][] roomBoundary;
    private final boolean[][] hallwayFloor;

    private static final int MIN_ROOM_WIDTH = 6;
    private static final int MAX_ROOM_WIDTH = 12;
    private static final int MIN_ROOM_HEIGHT = 6;
    private static final int MAX_ROOM_HEIGHT = 10;
    private static final int MAX_ROOM_ATTEMPTS = 5000;
    private static final double TARGET_STRUCTURE_RATIO = 0.50;
    private static final int MIN_ROOM_COUNT = 8;
    private static final int ROOM_MARGIN = 2;
    private static final int FLOOR_MARGIN = 2;

    private static final int[][] CARDINAL_DIRECTIONS = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1}
    };

    private static final int[][] ALL_DIRECTIONS = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1},
            {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
    };

    public World(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        this.seed = seed;
        this.random = new Random(seed);
        this.world = new TETile[width][height];
        this.rooms = new LinkedHashSet<>();
        this.roomInterior = new boolean[width][height];
        this.roomBoundary = new boolean[width][height];
        this.hallwayFloor = new boolean[width][height];
    }

    public TETile[][] generateWorld() {
        initializeWorld();
        generateRooms();
        connectRooms();
        addHallwayWalls();
        addWallsAroundAllFloors();
        return world;
    }

    private void initializeWorld() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                world[x][y] = Tileset.NOTHING;
                roomInterior[x][y] = false;
                roomBoundary[x][y] = false;
                hallwayFloor[x][y] = false;
            }
        }
    }

    private void generateRooms() {
        int attempts = 0;
        int structuredArea = 0;
        int targetArea = (int) (width * height * TARGET_STRUCTURE_RATIO);

        while (attempts < MAX_ROOM_ATTEMPTS
                && (structuredArea < targetArea || rooms.size() < MIN_ROOM_COUNT)) {
            Room candidateRoom = randomRoom();
            if (isValidRoom(candidateRoom)) {
                rooms.add(candidateRoom);
                carveRoom(candidateRoom);
                structuredArea += candidateRoom.area();
            }
            attempts++;
        }
    }

    private Room randomRoom() {
        int roomWidth = random.nextInt(MAX_ROOM_WIDTH - MIN_ROOM_WIDTH + 1) + MIN_ROOM_WIDTH;
        int roomHeight = random.nextInt(MAX_ROOM_HEIGHT - MIN_ROOM_HEIGHT + 1) + MIN_ROOM_HEIGHT;
        int roomX = random.nextInt(width - roomWidth - 2) + FLOOR_MARGIN;
        int roomY = random.nextInt(height - roomHeight - 2) + FLOOR_MARGIN;
        return new Room(roomX, roomY, roomWidth, roomHeight);
    }

    private boolean isValidRoom(Room candidateRoom) {
        if (candidateRoom.left() <= FLOOR_MARGIN || candidateRoom.right() >= width - FLOOR_MARGIN) {
            return false;
        }
        if (candidateRoom.bottom() <= FLOOR_MARGIN || candidateRoom.top() >= height - FLOOR_MARGIN) {
            return false;
        }

        for (Room room : rooms) {
            if (roomsTooClose(candidateRoom, room, ROOM_MARGIN)) {
                return false;
            }
        }
        return true;
    }

    private boolean roomsTooClose(Room a, Room b, int margin) {
        return a.right() + margin >= b.left()
                && b.right() + margin >= a.left()
                && a.top() + margin >= b.bottom()
                && b.top() + margin >= a.bottom();
    }

    private void carveRoom(Room room) {
        for (int x = room.left(); x <= room.right(); x++) {
            for (int y = room.bottom(); y <= room.top(); y++) {
                if (x == room.left() || x == room.right() || y == room.bottom() || y == room.top()) {
                    world[x][y] = Tileset.WALL;
                    roomBoundary[x][y] = true;
                } else {
                    world[x][y] = Tileset.FLOOR;
                    roomInterior[x][y] = true;
                }
            }
        }
    }

    private void connectRooms() {
        List<Room> orderedRooms = new ArrayList<>(rooms);
        for (int i = 0; i < orderedRooms.size() - 1; i++) {
            connectTwoRooms(orderedRooms.get(i), orderedRooms.get(i + 1));
        }
    }

    private void connectTwoRooms(Room a, Room b) {
        List<Position> bestPath = findBestLPath(a, b);
        if (bestPath == null) {
            bestPath = findBestBfsPath(a, b);
        }
        if (bestPath != null) {
            carveHallwayPath(bestPath);
        }
    }

    //This method is from ChatGPT 5.4
    private List<Position> findBestLPath(Room a, Room b) {
        List<Position> best = null;
        int bestLength = Integer.MAX_VALUE;

        for (int sideA = 0; sideA < 4; sideA++) {
            for (int sideB = 0; sideB < 4; sideB++) {
                for (int trial = 0; trial < 3; trial++) {
                    Position start = doorOnSide(a, sideA);
                    Position end = doorOnSide(b, sideB);

                    List<Position> horizontalFirst = buildLPath(start, end, true);
                    if (isValidHallwayPath(horizontalFirst) && horizontalFirst.size() < bestLength) {
                        best = horizontalFirst;
                        bestLength = horizontalFirst.size();
                    }

                    List<Position> verticalFirst = buildLPath(start, end, false);
                    if (isValidHallwayPath(verticalFirst) && verticalFirst.size() < bestLength) {
                        best = verticalFirst;
                        bestLength = verticalFirst.size();
                    }
                }
            }
        }
        return best;
    }

    //This method is from ChatGPT 5.4
    private List<Position> findBestBfsPath(Room a, Room b) {
        List<Position> best = null;
        int bestLength = Integer.MAX_VALUE;

        for (int sideA = 0; sideA < 4; sideA++) {
            for (int sideB = 0; sideB < 4; sideB++) {
                for (int trial = 0; trial < 2; trial++) {
                    Position start = doorOnSide(a, sideA);
                    Position end = doorOnSide(b, sideB);
                    List<Position> path = bfsPath(start, end);
                    if (path != null && path.size() < bestLength) {
                        best = path;
                        bestLength = path.size();
                    }
                }
            }
        }
        return best;
    }

    private Position doorOnSide(Room room, int side) {
        if (side == 0) {
            return room.leftDoor(random);
        } else if (side == 1) {
            return room.rightDoor(random);
        } else if (side == 2) {
            return room.bottomDoor(random);
        } else {
            return room.topDoor(random);
        }
    }

    private List<Position> buildLPath(Position start, Position end, boolean horizontalFirst) {
        List<Position> path = new ArrayList<>();
        path.add(start);

        if (horizontalFirst) {
            int xStep = Integer.compare(end.x, start.x);
            for (int x = start.x + xStep; x != end.x + xStep; x += xStep) {
                path.add(new Position(x, start.y));
            }
            int yStep = Integer.compare(end.y, start.y);
            for (int y = start.y + yStep; y != end.y + yStep; y += yStep) {
                if (y != start.y) {
                    path.add(new Position(end.x, y));
                }
            }
        } else {
            int yStep = Integer.compare(end.y, start.y);
            for (int y = start.y + yStep; y != end.y + yStep; y += yStep) {
                path.add(new Position(start.x, y));
            }
            int xStep = Integer.compare(end.x, start.x);
            for (int x = start.x + xStep; x != end.x + xStep; x += xStep) {
                if (x != start.x) {
                    path.add(new Position(x, end.y));
                }
            }
        }

        deduplicateConsecutive(path);
        return path;
    }

    private void deduplicateConsecutive(List<Position> path) {
        int i = 1;
        while (i < path.size()) {
            Position prev = path.get(i - 1);
            Position curr = path.get(i);
            if (prev.x == curr.x && prev.y == curr.y) {
                path.remove(i);
            } else {
                i++;
            }
        }
    }

    private boolean isValidHallwayPath(List<Position> path) {
        if (path == null || path.isEmpty()) {
            return false;
        }

        for (int i = 0; i < path.size(); i++) {
            Position p = path.get(i);
            if (!inInnerBounds(p.x, p.y)) {
                return false;
            }

            boolean endpoint = (i == 0 || i == path.size() - 1);
            if (!endpoint && (roomInterior[p.x][p.y] || roomBoundary[p.x][p.y])) {
                return false;
            }
        }

        for (int i = 1; i < path.size(); i++) {
            Position a = path.get(i - 1);
            Position b = path.get(i);
            int dist = Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
            if (dist != 1) {
                return false;
            }
        }
        return true;
    }

    private List<Position> bfsPath(Position start, Position end) {
        boolean[][] visited = new boolean[width][height];
        Position[][] parent = new Position[width][height];
        ArrayDeque<Position> fringe = new ArrayDeque<>();

        fringe.add(start);
        visited[start.x][start.y] = true;

        while (!fringe.isEmpty()) {
            Position current = fringe.removeFirst();
            if (current.x == end.x && current.y == end.y) {
                return reconstructPath(parent, end);
            }

            for (int[] dir : CARDINAL_DIRECTIONS) {
                int nx = current.x + dir[0];
                int ny = current.y + dir[1];
                if (!inInnerBounds(nx, ny) || visited[nx][ny]) {
                    continue;
                }
                if (!canStepOn(nx, ny, start, end)) {
                    continue;
                }
                visited[nx][ny] = true;
                parent[nx][ny] = current;
                fringe.addLast(new Position(nx, ny));
            }
        }
        return null;
    }

    private boolean canStepOn(int x, int y, Position start, Position end) {
        if ((x == start.x && y == start.y) || (x == end.x && y == end.y)) {
            return true;
        }
        if (roomInterior[x][y] || roomBoundary[x][y]) {
            return false;
        }
        return world[x][y] == Tileset.NOTHING || hallwayFloor[x][y];
    }

    private List<Position> reconstructPath(Position[][] parent, Position end) {
        ArrayList<Position> reversed = new ArrayList<>();
        Position current = end;
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

    private void carveHallwayPath(List<Position> path) {
        for (Position p : path) {
            world[p.x][p.y] = Tileset.FLOOR;
            hallwayFloor[p.x][p.y] = true;
            roomBoundary[p.x][p.y] = false;
        }
    }

    private void addHallwayWalls() {
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                if (!hallwayFloor[x][y]) {
                    continue;
                }

                for (int[] dir : ALL_DIRECTIONS) {
                    int nx = x + dir[0];
                    int ny = y + dir[1];

                    if (inBounds(nx, ny) && world[nx][ny] == Tileset.NOTHING) {
                        world[nx][ny] = Tileset.WALL;
                    }
                }
            }
        }
    }

    private void addWallsAroundAllFloors() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (world[x][y] != Tileset.FLOOR) {
                    continue;
                }

                for (int[] dir : ALL_DIRECTIONS) {
                    int nx = x + dir[0];
                    int ny = y + dir[1];

                    if (inBounds(nx, ny) && world[nx][ny] == Tileset.NOTHING) {
                        world[nx][ny] = Tileset.WALL;
                    }
                }
            }
        }
    }

    private boolean inInnerBounds(int x, int y) {
        return x > FLOOR_MARGIN
                && x < width - FLOOR_MARGIN
                && y > FLOOR_MARGIN
                && y < height - FLOOR_MARGIN;
    }

    private boolean inBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
}
