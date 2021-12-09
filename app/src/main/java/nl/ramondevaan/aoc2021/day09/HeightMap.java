package nl.ramondevaan.aoc2021.day09;

public class HeightMap {
    private final int height;
    private final int width;
    private final int[][] heights;

    public HeightMap(int height, int width, int[][] heights) {
        this.height = height;
        this.width = width;
        this.heights = heights;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public boolean isInRange(Coordinate coordinate) {
        int x = coordinate.x();
        int y = coordinate.y();

        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public int valueAt(Coordinate coordinate) {
        return this.heights[coordinate.y()][coordinate.x()];
    }
}
