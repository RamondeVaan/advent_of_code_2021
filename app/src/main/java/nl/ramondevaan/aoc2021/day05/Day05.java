package nl.ramondevaan.aoc2021.day05;

import java.util.List;
import java.util.stream.Stream;

import static java.lang.Integer.signum;

public class Day05 {

    private final List<LineSegment> lineSegments;
    private final int xMax;
    private final int yMax;

    public Day05(List<String> lines) {
        LineSegmentParser lineSegmentParser = new LineSegmentParser();
        this.lineSegments = lines.stream().map(lineSegmentParser::parse).toList();

        int xMax = Integer.MIN_VALUE;
        int yMax = Integer.MIN_VALUE;
        for (LineSegment lineSegment : lineSegments) {
            xMax = Math.max(lineSegment.start().x(), xMax);
            xMax = Math.max(lineSegment.end().x(), xMax);
            yMax = Math.max(lineSegment.start().y(), yMax);
            yMax = Math.max(lineSegment.end().y(), yMax);
        }
        this.xMax = xMax + 1;
        this.yMax = yMax + 1;
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

    private long countPointsOfOverlap(Stream<LineSegment> lineSegmentStream) {
        int[][] grid = new int[xMax][yMax];

        lineSegmentStream.forEach(lineSegment -> {
            int distX = lineSegment.end().x() - lineSegment.start().x();
            int distY = lineSegment.end().y() - lineSegment.start().y();
            int distance = Math.max(Math.abs(distX), Math.abs(distY));
            int dx = signum(distX);
            int dy = signum(distY);

            int currentX = lineSegment.start().x();
            int currentY = lineSegment.start().y();

            grid[currentX][currentY]++;

            for (int i = 0; i < distance; i++) {
                currentX += dx;
                currentY += dy;

                grid[currentX][currentY]++;
            }
        });

        long count = 0;

        for (int x = 0; x < xMax; x++) {
            for (int y = 0; y < yMax; y++) {
                if (grid[x][y] > 1) {
                    count++;
                }
            }
        }

        return count;
    }
}
