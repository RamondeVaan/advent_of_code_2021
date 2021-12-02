package nl.ramondevaan.aoc2021.day02;

public class Position {
    private final int horizontal;
    private final int depth;
    private final int aim;

    public Position(int horizontal, int depth, int aim) {
        this.horizontal = horizontal;
        this.depth = depth;
        this.aim = aim;
    }

    public int getHorizontal() {
        return horizontal;
    }

    public int getDepth() {
        return depth;
    }

    public int getAim() {
        return aim;
    }

    public Position withHorizontal(int horizontal) {
        return new Position(horizontal, depth, aim);
    }

    public Position withDepth(int depth) {
        return new Position(horizontal, depth, aim);
    }

    public Position withAim(int aim) {
        return new Position(horizontal, depth, aim);
    }
}
