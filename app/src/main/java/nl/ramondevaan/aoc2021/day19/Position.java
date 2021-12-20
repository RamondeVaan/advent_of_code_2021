package nl.ramondevaan.aoc2021.day19;

public record Position(int x, int y, int z) {

    public int distanceSquared(Position other) {
        int xDiff = x - other.x;
        int yDiff = y - other.y;
        int zDiff = z - other.z;

        return xDiff * xDiff + yDiff * yDiff + zDiff * zDiff;
    }

    public int manhattanDistance(Position other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(z - other.z);
    }
}
