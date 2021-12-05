package nl.ramondevaan.aoc2021.day05;

public class LineSegment {
    public final Position start;
    public final Position end;

    public LineSegment(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    public Position getStart() {
        return start;
    }

    public Position getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return String.format("%d,%d -> %d,%d", start.x, start.y, end.x, end.y);
    }
}
