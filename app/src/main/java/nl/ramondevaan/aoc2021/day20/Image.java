package nl.ramondevaan.aoc2021.day20;

import nl.ramondevaan.aoc2021.util.Coordinate;

public class Image {
    private final boolean[][] pixels;
    private final int rows;
    private final int columns;
    private final boolean gridPixelsAreLight;

    private Image(boolean[][] pixels, int rows, int columns, boolean gridPixelsAreLight) {
        this.pixels = pixels;
        this.rows = rows;
        this.columns = columns;
        this.gridPixelsAreLight = gridPixelsAreLight;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public boolean gridPixelsAreLight() {
        return gridPixelsAreLight;
    }

    public boolean isLight(int row, int column) {
        if (row >= 0 && row < rows && column >= 0 && column < columns) {
            return pixels[row][column];
        }
        return gridPixelsAreLight;
    }

    public static Builder builder(int rows, int columns) {
        return new Builder(rows, columns);
    }

    public static class Builder {
        private final boolean[][] pixels;
        private final int rows;
        private final int columns;
        private boolean gridPixelsAreLight;

        private Builder(int rows, int columns) {
            this.pixels = new boolean[rows][columns];
            this.rows = rows;
            this.columns = columns;
            this.gridPixelsAreLight = false;
        }

        public void set(int row, int column, boolean isLight) {
            pixels[row][column] = isLight;
        }

        public void set(Coordinate coordinate, boolean isLight) {
            pixels[coordinate.row()][coordinate.column()] = isLight;
        }

        public void setGridPixelsAreLight(boolean gridPixelsAreLight) {
            this.gridPixelsAreLight = gridPixelsAreLight;
        }

        public Image build() {
            return new Image(pixels, rows, columns, gridPixelsAreLight);
        }
    }
}
