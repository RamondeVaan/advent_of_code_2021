package nl.ramondevaan.aoc2021.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoordinateIntegerMapParser implements Parser<List<String>, Map<Coordinate, Integer>> {
    @Override
    public Map<Coordinate, Integer> parse(List<String> toParse) {
        Map<Coordinate, Integer> grid = new HashMap<>();

        for (int row = 0; row < toParse.size(); row++) {
            char[] chars = toParse.get(row).toCharArray();
            for (int column = 0; column < chars.length; column++) {
                grid.put(Coordinate.of(row, column), chars[column] - '0');
            }
        }

        return Collections.unmodifiableMap(grid);
    }
}
