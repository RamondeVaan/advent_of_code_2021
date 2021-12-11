package nl.ramondevaan.aoc2021.day05;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day05 {

    private final List<LineSegment> lineSegments;

    public Day05(List<String> lines) {
        LineSegmentParser lineSegmentParser = new LineSegmentParser();

        this.lineSegments = lines.stream().map(lineSegmentParser::parse).toList();
    }

    public long solve1() {
        Stream<LineSegment> lineSegmentStream = lineSegments.stream()
                .filter(lineSegment -> lineSegment.start().x() == lineSegment.end().x() ||
                        lineSegment.start().y() == lineSegment.end().y());
        return countPointsOfOverlap(lineSegmentStream);
    }

    public long solve2() {
        return countPointsOfOverlap(lineSegments.stream());
    }

    private static long countPointsOfOverlap(Stream<LineSegment> lineSegmentStream) {
        PositionsParser positionsParser = new PositionsParser();

        Map<Position, Long> occurences = lineSegmentStream
                .map(positionsParser::parse)
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));

        return occurences.entrySet().stream().filter(entry -> entry.getValue() > 1).count();
    }
}
