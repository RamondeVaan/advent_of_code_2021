package nl.ramondevaan.aoc2021.day13;

public record Dot(int x, int y) {
    public Dot fold(Fold fold) {
        int value = fold.value();
        return switch (fold.axis()) {
            case X -> new Dot(x < value ? x : 2 * value - x, y);
            case Y -> new Dot(x, y < value ? y : 2 * value - y);
        };
    }
}
