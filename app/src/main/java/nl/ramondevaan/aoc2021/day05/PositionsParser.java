package nl.ramondevaan.aoc2021.day05;

import nl.ramondevaan.aoc2021.util.Parser;

import java.util.List;
import java.util.stream.Stream;

import static java.lang.Integer.signum;

public class PositionsParser implements Parser<LineSegment, List<Position>> {
    @Override
    public List<Position> parse(LineSegment toParse) {
        int distX = toParse.end().x() - toParse.start().x();
        int distY = toParse.end().y() - toParse.start().y();
        int distance = Math.max(Math.abs(distX), Math.abs(distY));
        int dx = signum(distX);
        int dy = signum(distY);

        return Stream.iterate(toParse.start(), position -> new Position(position.x() + dx, position.y() + dy))
                .limit(distance + 1).toList();
    }
}
