package nl.ramondevaan.aoc2021.day19;

import java.util.function.Function;

public enum Rotation {
    X0Y0(Function.identity()),
    X0Y1(beacon -> rotateY(beacon, 1)),
    X0Y2(beacon -> rotateY(beacon, 2)),
    X0Y3(beacon -> rotateY(beacon, 3)),

    X1Y0(beacon -> rotateX(beacon, 1)),
    X1Y1(beacon -> rotateY(rotateX(beacon, 1), 1)),
    X1Y2(beacon -> rotateY(rotateX(beacon, 1), 2)),
    X1Y3(beacon -> rotateY(rotateX(beacon, 1), 3)),

    X2Y0(beacon -> rotateX(beacon, 2)),
    X2Y1(beacon -> rotateY(rotateX(beacon, 2), 1)),
    X2Y2(beacon -> rotateY(rotateX(beacon, 2), 2)),
    X2Y3(beacon -> rotateY(rotateX(beacon, 2), 3)),

    X3Y0(beacon -> rotateX(beacon, 3)),
    X3Y1(beacon -> rotateY(rotateX(beacon, 3), 1)),
    X3Y2(beacon -> rotateY(rotateX(beacon, 3), 2)),
    X3Y3(beacon -> rotateY(rotateX(beacon, 3), 3)),

    Z1Y0(beacon -> rotateZ(beacon, 1)),
    Z1Y1(beacon -> rotateY(rotateZ(beacon, 1), 1)),
    Z1Y2(beacon -> rotateY(rotateZ(beacon, 1), 2)),
    Z1Y3(beacon -> rotateY(rotateZ(beacon, 1), 3)),

    Z3Y0(beacon -> rotateZ(beacon, 3)),
    Z3Y1(beacon -> rotateY(rotateZ(beacon, 3), 1)),
    Z3Y2(beacon -> rotateY(rotateZ(beacon, 3), 2)),
    Z3Y3(beacon -> rotateY(rotateZ(beacon, 3), 3));

    private final Function<Position, Position> function;

    Rotation(Function<Position, Position> function) {
        this.function = function;
    }

    public Position apply(Position position) {
        return function.apply(position);
    }

    private static Position rotateX(Position position, int times) {
        int t = Math.floorMod(times, 4);
        return switch (t) {
            case 0 -> position;
            case 1 -> new Position(position.x(), position.z(), -position.y());
            case 2 -> new Position(position.x(), -position.y(), -position.z());
            case 3 -> new Position(position.x(), -position.z(), position.y());
            default -> throw new IllegalStateException();
        };
    }

    private static Position rotateY(Position position, int times) {
        int t = Math.floorMod(times, 4);
        return switch (t) {
            case 0 -> position;
            case 1 -> new Position(-position.z(), position.y(), position.x());
            case 2 -> new Position(-position.x(), position.y(), -position.z());
            case 3 -> new Position(position.z(), position.y(), -position.x());
            default -> throw new IllegalStateException();
        };
    }

    private static Position rotateZ(Position position, int times) {
        int t = Math.floorMod(times, 4);
        return switch (t) {
            case 0 -> position;
            case 1 -> new Position(position.y(), -position.x(), position.z());
            case 2 -> new Position(-position.x(), -position.y(), position.z());
            case 3 -> new Position(-position.y(), position.x(), position.z());
            default -> throw new IllegalStateException();
        };
    }
}
