package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.Random;

public class Encounter {
    private static final int ENCOUNTER_COUNT = 6;
    private static final int COINS_TO_WIN = 22;
    private static final TETile MONSTER = Tileset.MONSTER;
    private static final int MONSTER_MOVE_INTERVAL = 450;
    private static final int MONSTER_CHASE_RANGE = 18;
    private static final double MONSTER_CHASE_CHANCE = 1;

    private static final int[][] MONSTER_DIRECTIONS = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1}
    };

    private int[] encounterX;
    private int[] encounterY;
    private boolean[] encounterUsed;

    private int currentEncounterId;
    private int coinsLeft;
    private int totalCoins;

    private int monsterX;
    private int monsterY;
    private int monsterDX;
    private int monsterDY;
    private TETile tileUnderMonster;
    private Random monsterRandom;
    private long lastMonsterMoveTime;

    public Encounter() {
        encounterX = new int[ENCOUNTER_COUNT];
        encounterY = new int[ENCOUNTER_COUNT];
        encounterUsed = new boolean[ENCOUNTER_COUNT];
        totalCoins = 0;
    }

    public void reset() {
        totalCoins = 0;
        coinsLeft = 0;
        currentEncounterId = 0;
        for (int i = 0; i < ENCOUNTER_COUNT; i++) {
            encounterUsed[i] = false;
        }
    }

    public void placeEncounterEntrances(TETile[][] world, int width, int height,
                                        int playerX, int playerY, long seed) {
        Random r = new Random(seed + 999);

        for (int i = 0; i < ENCOUNTER_COUNT; i++) {
            encounterUsed[i] = false;

            while (true) {
                int x = r.nextInt(width);
                int y = r.nextInt(height);

                if (validEncounterEntrance(world, x, y, playerX, playerY, i)) {
                    encounterX[i] = x;
                    encounterY[i] = y;
                    world[x][y] = Tileset.LOCKED_DOOR;
                    break;
                }
            }
        }
    }

    private boolean validEncounterEntrance(TETile[][] world, int x, int y,
                                           int playerX, int playerY, int count) {
        if (world[x][y] != Tileset.FLOOR) {
            return false;
        }

        if (x == playerX && y == playerY) {
            return false;
        }

        for (int i = 0; i < count; i++) {
            int distance = Math.abs(x - encounterX[i]) + Math.abs(y - encounterY[i]);
            if (distance < 6) {
                return false;
            }
        }

        return true;
    }

    public int encounterAt(int x, int y) {
        for (int i = 0; i < ENCOUNTER_COUNT; i++) {
            if (!encounterUsed[i] && x == encounterX[i] && y == encounterY[i]) {
                return i;
            }
        }
        return -1;
    }

    public void markEncounterUsed(TETile[][] mainWorld, int encounterId) {
        encounterUsed[encounterId] = true;
        mainWorld[encounterX[encounterId]][encounterY[encounterId]] = Tileset.FLOOR;
    }

    public TETile[][] createEncounterWorld(int encounterId, int width, int height) {
        currentEncounterId = encounterId;

        TETile[][] ew = new TETile[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                ew[x][y] = Tileset.NOTHING;
            }
        }

        coinsLeft = 0;

        if (encounterId == 0) {
            carveEncounterRoom(ew, 18, 52, 9, 25);

            addCoin(ew, 24, 13);
            addCoin(ew, 30, 21);
            addCoin(ew, 44, 20);
            addCoin(ew, 49, 12);

        } else if (encounterId == 1) {
            carveEncounterRoom(ew, 12, 57, 6, 28);

            addCoin(ew, 15, 8);
            addCoin(ew, 20, 26);
            addCoin(ew, 34, 25);
            addCoin(ew, 50, 9);
            addCoin(ew, 55, 20);

        } else if (encounterId == 2) {
            carveEncounterRoom(ew, 20, 50, 5, 29);

            addCoin(ew, 22, 7);
            addCoin(ew, 24, 27);
            addCoin(ew, 30, 17);
            addCoin(ew, 45, 26);
            addCoin(ew, 48, 10);

        } else if (encounterId == 3) {
            carveEncounterRoom(ew, 10, 60, 10, 26);

            addCoin(ew, 13, 12);
            addCoin(ew, 18, 24);
            addCoin(ew, 26, 14);
            addCoin(ew, 44, 24);
            addCoin(ew, 55, 13);
            addCoin(ew, 58, 22);

        } else if (encounterId == 4) {
            carveEncounterRoom(ew, 16, 54, 4, 30);

            addCoin(ew, 18, 6);
            addCoin(ew, 21, 28);
            addCoin(ew, 29, 10);
            addCoin(ew, 41, 27);
            addCoin(ew, 48, 8);
            addCoin(ew, 52, 20);

        } else {
            carveEncounterRoom(ew, 8, 62, 7, 27);

            addCoin(ew, 10, 9);
            addCoin(ew, 14, 25);
            addCoin(ew, 22, 11);
            addCoin(ew, 29, 24);
            addCoin(ew, 43, 10);
            addCoin(ew, 55, 25);
            addCoin(ew, 60, 15);
        }

        return ew;
    }

    private void carveEncounterRoom(TETile[][] ew, int left, int right, int bottom, int top) {
        for (int x = left; x <= right; x++) {
            for (int y = bottom; y <= top; y++) {
                if (x == left || x == right || y == bottom || y == top) {
                    ew[x][y] = Tileset.WALL;
                } else {
                    ew[x][y] = Tileset.FLOOR;
                }
            }
        }
    }

    private void addCoin(TETile[][] ew, int x, int y) {
        ew[x][y] = Tileset.COIN;
        coinsLeft++;
    }

    public void placeMonster(TETile[][] world, int width, int height,
                             int playerX, int playerY, long seed, int encounterId) {
        if (encounterId == 0) {
            monsterX = 20;
            monsterY = 10;
        } else if (encounterId == 1) {
            monsterX = 14;
            monsterY = 7;
        } else if (encounterId == 2) {
            monsterX = 21;
            monsterY = 6;
        } else if (encounterId == 3) {
            monsterX = 11;
            monsterY = 11;
        } else if (encounterId == 4) {
            monsterX = 17;
            monsterY = 5;
        } else {
            monsterX = 9;
            monsterY = 8;
        }

        if (world[monsterX][monsterY] != Tileset.FLOOR) {
            findMonsterStart(world, width, height, playerX, playerY);
        }

        monsterRandom = new Random(seed + encounterId * 1000 + 777);
        chooseMonsterDirection(world, width, height);

        tileUnderMonster = world[monsterX][monsterY];
        world[monsterX][monsterY] = MONSTER;

        lastMonsterMoveTime = System.currentTimeMillis();
    }

    private void findMonsterStart(TETile[][] world, int width, int height, int playerX, int playerY) {
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                if (world[x][y] == Tileset.FLOOR && distance(x, y, playerX, playerY) > 15) {
                    monsterX = x;
                    monsterY = y;
                    return;
                }
            }
        }
    }

    public boolean shouldMoveMonster() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastMonsterMoveTime >= MONSTER_MOVE_INTERVAL) {
            lastMonsterMoveTime = currentTime;
            return true;
        }
        return false;
    }

    public boolean moveMonster(TETile[][] world, int width, int height, int playerX, int playerY) {
        world[monsterX][monsterY] = tileUnderMonster;

        boolean shouldChase = distance(monsterX, monsterY, playerX, playerY) <= MONSTER_CHASE_RANGE
                && monsterRandom.nextDouble() < MONSTER_CHASE_CHANCE;

        if (shouldChase) {
            chooseChaseDirection(world, width, height, playerX, playerY);
        }

        int nextX = monsterX + monsterDX;
        int nextY = monsterY + monsterDY;

        if (!validMonsterMove(world, width, height, nextX, nextY)) {
            chooseMonsterDirection(world, width, height);
            nextX = monsterX + monsterDX;
            nextY = monsterY + monsterDY;
        }

        if (!validMonsterMove(world, width, height, nextX, nextY)) {
            tileUnderMonster = world[monsterX][monsterY];
            world[monsterX][monsterY] = MONSTER;
            return false;
        }

        if (nextX == playerX && nextY == playerY) {
            return true;
        }

        tileUnderMonster = world[nextX][nextY];
        monsterX = nextX;
        monsterY = nextY;
        world[monsterX][monsterY] = MONSTER;

        return false;
    }

    private void chooseMonsterDirection(TETile[][] world, int width, int height) {
        for (int i = 0; i < 20; i++) {
            int[] direction = MONSTER_DIRECTIONS[monsterRandom.nextInt(MONSTER_DIRECTIONS.length)];
            int nextX = monsterX + direction[0];
            int nextY = monsterY + direction[1];

            if (validMonsterMove(world, width, height, nextX, nextY)) {
                monsterDX = direction[0];
                monsterDY = direction[1];
                return;
            }
        }

        monsterDX = 0;
        monsterDY = 0;
    }

    private void chooseChaseDirection(TETile[][] world, int width, int height,
                                      int playerX, int playerY) {
        int bestDX = 0;
        int bestDY = 0;
        int bestDistance = distance(monsterX, monsterY, playerX, playerY);

        for (int[] direction : MONSTER_DIRECTIONS) {
            int nextX = monsterX + direction[0];
            int nextY = monsterY + direction[1];

            if (!validMonsterMove(world, width, height, nextX, nextY)) {
                continue;
            }

            int nextDistance = distance(nextX, nextY, playerX, playerY);
            if (nextDistance < bestDistance) {
                bestDistance = nextDistance;
                bestDX = direction[0];
                bestDY = direction[1];
            }
        }

        if (bestDX != 0 || bestDY != 0) {
            monsterDX = bestDX;
            monsterDY = bestDY;
        } else {
            chooseMonsterDirection(world, width, height);
        }
    }

    private boolean validMonsterMove(TETile[][] world, int width, int height, int x, int y) {
        if ((x < 0) || x >= width || y < 0 || y >= height) {
            return false;
        }

        return world[x][y] == Tileset.FLOOR
                || world[x][y] == Tileset.COIN
                || world[x][y] == Tileset.AVATAR;
    }

    public boolean isMonster(TETile tile) {
        return tile == MONSTER;
    }

    public boolean isCoin(TETile tile) {
        return tile == Tileset.COIN;
    }

    public void collectCoin() {
        coinsLeft--;
        totalCoins++;
    }

    public boolean roomCleared() {
        return coinsLeft == 0;
    }

    public boolean hasWon() {
        return totalCoins >= COINS_TO_WIN;
    }

    public int getCurrentEncounterId() {
        return currentEncounterId;
    }

    public int getCoinsLeft() {
        return coinsLeft;
    }

    public int getTotalCoins() {
        return totalCoins;
    }

    public int getCoinsToWin() {
        return COINS_TO_WIN;
    }

    private int distance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
}
