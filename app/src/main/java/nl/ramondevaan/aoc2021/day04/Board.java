package nl.ramondevaan.aoc2021.day04;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Board {
    private final Map<Integer, Position> numberPositionMap;
    private final int rows;
    private final int columns;

    public Board(int[] values, int rows) {
        this.rows = rows;
        this.columns = values.length / rows;
        this.numberPositionMap = new HashMap<>(values.length);

        int count = 0;
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < this.columns; column++) {
                this.numberPositionMap.put(values[count++], Position.of(row, column));
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Map<Integer, Position> getNumberPositionMap() {
        return Collections.unmodifiableMap(numberPositionMap);
    }
}
