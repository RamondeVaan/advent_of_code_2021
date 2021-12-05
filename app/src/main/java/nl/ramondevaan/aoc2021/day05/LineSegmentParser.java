package nl.ramondevaan.aoc2021.day05;

import nl.ramondevaan.aoc2021.util.Parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineSegmentParser implements Parser<String, LineSegment> {

    private static final Pattern LINE_SEGMENT_PATTERN = Pattern.compile(
            "(?<x1>\\d+),(?<y1>\\d+) -> (?<x2>\\d+),(?<y2>\\d+)");

    @Override
    public LineSegment parse(String toParse) {
        Matcher matcher = LINE_SEGMENT_PATTERN.matcher(toParse);

        if (!matcher.matches()) {
            throw new IllegalArgumentException();
        }

        return new LineSegment(
                Position.of(Integer.parseInt(matcher.group("x1")), Integer.parseInt(matcher.group("y1"))),
                Position.of(Integer.parseInt(matcher.group("x2")), Integer.parseInt(matcher.group("y2")))
        );
    }
}
