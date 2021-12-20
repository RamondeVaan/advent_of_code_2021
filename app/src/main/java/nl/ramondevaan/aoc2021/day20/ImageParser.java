package nl.ramondevaan.aoc2021.day20;

import nl.ramondevaan.aoc2021.util.Parser;

import java.util.List;

public class ImageParser implements Parser<List<String>, Image> {
    @Override
    public Image parse(List<String> toParse) {
        int rows = toParse.size();
        int columns = toParse.stream().mapToInt(String::length).max().orElseThrow();

        Image.Builder builder = Image.builder(rows, columns);
        builder.setGridPixelsAreLight(false);

        for (int row = 0; row < rows; row++) {
            String line = toParse.get(row);
            for (int column = 0; column < columns; column++) {
                builder.set(row, column, line.charAt(column) == '#');
            }
        }

        return builder.build();
    }
}
