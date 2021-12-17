package nl.ramondevaan.aoc2021.day17;

import nl.ramondevaan.aoc2021.util.Position;

public record TargetArea(int xMin, int xMax, int yMin, int yMax) {

    public boolean contains(Position position) {
        return position.x() >= xMin && position.x() <= xMax && position.y() >= yMin && position.y() <= yMax;
    }
}
