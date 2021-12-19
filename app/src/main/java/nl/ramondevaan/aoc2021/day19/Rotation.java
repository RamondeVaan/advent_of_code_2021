package nl.ramondevaan.aoc2021.day19;

import java.util.function.BiFunction;
import java.util.function.Function;

public enum Rotation {
    X0Y0(0, 0, 0),
    X0Y1(0, 1, 0),
    X0Y2(0, 2, 0),
    X0Y3(0, 3, 0),

    X1Y0(1, 0, 0),
    X1Y1(1, 1, 0),
    X1Y2(1, 2, 0),
    X1Y3(1, 3, 0),

    X2Y0(2, 0, 0),
    X2Y1(2, 1, 0),
    X2Y2(2, 2, 0),
    X2Y3(2, 3, 0),

    X3Y0(3, 0, 0),
    X3Y1(3, 1, 0),
    X3Y2(3, 2, 0),
    X3Y3(3, 3, 0),

    Z1Y0(0, 0, 1),
    Z1Y1(0, 1, 1),
    Z1Y2(0, 2, 1),
    Z1Y3(0, 3, 1),

    Z3Y0(0, 0, 3),
    Z3Y1(0, 1, 3),
    Z3Y2(0, 2, 3),
    Z3Y3(0, 3, 3);

    private final int x;
    private final int y;
    private final int z;

    Rotation(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Beacon apply(Beacon beacon) {
        return rotateY(rotateX(rotateZ(beacon, z), x), y);
    }

    private static Beacon rotateX(Beacon beacon, int times) {
        int t = Math.floorMod(times, 4);
        return switch (t) {
            case 0 -> beacon;
            case 1 -> new Beacon(beacon.x(), beacon.z(), -beacon.y());
            case 2 -> new Beacon(beacon.x(), -beacon.y(), -beacon.z());
            case 3 -> new Beacon(beacon.x(), -beacon.z(), beacon.y());
            default -> throw new IllegalStateException();
        };
    }

    private static Beacon rotateY(Beacon beacon, int times) {
        int t = Math.floorMod(times, 4);
        return switch (t) {
            case 0 -> beacon;
            case 1 -> new Beacon(-beacon.z(), beacon.y(), beacon.x());
            case 2 -> new Beacon(-beacon.x(), beacon.y(), -beacon.z());
            case 3 -> new Beacon(beacon.z(), beacon.y(), -beacon.x());
            default -> throw new IllegalStateException();
        };
    }

    private static Beacon rotateZ(Beacon beacon, int times) {
        int t = Math.floorMod(times, 4);
        return switch (t) {
            case 0 -> beacon;
            case 1 -> new Beacon(beacon.y(), -beacon.x(), beacon.z());
            case 2 -> new Beacon(-beacon.x(), -beacon.y(), beacon.z());
            case 3 -> new Beacon(-beacon.y(), beacon.x(), beacon.z());
            default -> throw new IllegalStateException();
        };
    }
}
