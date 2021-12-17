package nl.ramondevaan.aoc2021.util;

public record Position(int x, int y) {
    public Position add(Position position) {
        return new Position(this.x + position.x, this.y + position.y);
    }
}
