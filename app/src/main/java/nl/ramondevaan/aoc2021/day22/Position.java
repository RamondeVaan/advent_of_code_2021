package nl.ramondevaan.aoc2021.day22;

public record Position(int x, int y, int z) {
    public Position offset(int xOffset, int yOffset, int zOffset) {
        return new Position(x + xOffset, y + yOffset, z + zOffset);
    }
}
