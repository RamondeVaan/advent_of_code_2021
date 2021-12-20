package nl.ramondevaan.aoc2021.day20;

import com.google.common.math.IntMath;

import java.util.List;
import java.util.stream.Stream;

public class Day20 {

    private final List<Boolean> algorithm;
    private final Image image;

    public Day20(List<String> lines) {
        AlgorithmParser algorithmParser = new AlgorithmParser();
        ImageParser imageParser = new ImageParser();
        this.algorithm = algorithmParser.parse(lines.get(0));
        this.image = imageParser.parse(lines.subList(2, lines.size()));
    }

    public long solve1() {
        return solve(2);
    }

    public long solve2() {
        return solve(50);
    }

    public long solve(int iterations) {
        Image result = Stream.iterate(image, this::next)
                .skip(iterations).limit(1)
                .findAny().orElseThrow();

        long count = 0;

        for (int row = 0; row < result.getRows(); row++) {
            for (int column = 0; column < result.getColumns(); column++) {
                count += result.isLight(row, column) ? 1 : 0;
            }
        }

        return count;
    }

    private Image next(Image current) {
        int newRows = current.getRows() + 2;
        int newColumns = current.getColumns() + 2;

        Image.Builder builder = Image.builder(newRows, newColumns);
        builder.setGridPixelsAreLight(current.gridPixelsAreLight() ? algorithm.get(511) : algorithm.get(0));

        for (int row = 0, oldRow = -1; row < newRows; row++, oldRow++) {
            for (int column = 0, oldColumn = -1; column < newColumns; column++, oldColumn++) {
                builder.set(row, column, isLight(current, oldRow, oldColumn));
            }
        }

        return builder.build();
    }

    private boolean isLight(Image image, int row, int column) {
        int pow = 8;
        int index = 0;

        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                index += image.isLight(row + rowOffset, column + columnOffset) ? IntMath.pow(2, pow) : 0;
                pow--;
            }
        }

        return algorithm.get(index);
    }
}
