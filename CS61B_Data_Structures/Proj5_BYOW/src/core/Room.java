package core;

import java.util.Objects;
import java.util.Random;

public class Room {
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public Room(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int left() {
        return x;
    }

    public int right() {
        return x + width - 1;
    }

    public int bottom() {
        return y;
    }

    public int top() {
        return y + height - 1;
    }

    public int area() {
        return width * height;
    }

    public Position center() {
        return new Position(x + width / 2, y + height / 2);
    }

    public boolean overlapsOrTooClose(Room other) {
        return this.right() + 1 >= other.left()
                && other.right() + 1 >= this.left()
                && this.top() + 1 >= other.bottom()
                && other.top() + 1 >= this.bottom();
    }

    public Position leftDoor(Random random) {
        int doorY = random.nextInt(height - 2) + y + 1;
        return new Position(left(), doorY);
    }

    public Position rightDoor(Random random) {
        int doorY = random.nextInt(height - 2) + y + 1;
        return new Position(right(), doorY);
    }

    public Position topDoor(Random random) {
        int doorX = random.nextInt(width - 2) + x + 1;
        return new Position(doorX, top());
    }

    public Position bottomDoor(Random random) {
        int doorX = random.nextInt(width - 2) + x + 1;
        return new Position(doorX, bottom());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Room other)) {
            return false;
        }
        return x == other.x && y == other.y && width == other.width && height == other.height;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, width, height);
    }
}
