package nl.ramondevaan.aoc2021.day25;

import nl.ramondevaan.aoc2021.util.Coordinate;
import nl.ramondevaan.aoc2021.util.IntMap;
import nl.ramondevaan.aoc2021.util.MutableIntMap;

import java.util.List;

public class Day25 {

    private final static int SOUTH = 'v';
    private final static int EAST = '>';
    private final IntMap region;

    public Day25(List<String> lines) {
        RegionParser parser = new RegionParser();
        this.region = parser.parse(lines);
    }

    public long solve() {
        MutableIntMap current = new MutableIntMap(region);
        MutableIntMap next = move(current);

        int count = 1;
        for (; !next.equals(current); count++) {
            current = next;
            next = move(current);
        }

        return count;
    }

    private static MutableIntMap move(MutableIntMap in) {
        MutableIntMap out = new MutableIntMap(in.rows(), in.columns());

        moveEast(in, out);
        moveSouth(in, out);

        return out;
    }

    private static void moveEast(MutableIntMap in, MutableIntMap out) {
        in.keys().filter(coordinate -> in.valueAt(coordinate) == EAST).forEach(coordinate -> {
            Coordinate next = new Coordinate(coordinate.row(), (coordinate.column() + 1) % in.columns());
            out.setValueAt(in.valueAt(next) == 0 ? next : coordinate, EAST);
        });
    }

    private static void moveSouth(MutableIntMap in, MutableIntMap out) {
        in.keys().filter(coordinate -> in.valueAt(coordinate) == SOUTH).forEach(coordinate -> {
            Coordinate next = new Coordinate((coordinate.row() + 1) % in.rows(), coordinate.column());
            if (in.valueAt(next) == SOUTH || out.valueAt(next) == EAST) {
                out.setValueAt(coordinate, SOUTH);
            } else {
                out.setValueAt(next, SOUTH);
            }
        });
    }
}
