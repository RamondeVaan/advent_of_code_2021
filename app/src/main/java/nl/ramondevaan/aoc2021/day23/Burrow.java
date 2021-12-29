package nl.ramondevaan.aoc2021.day23;

import com.google.common.math.LongMath;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public class Burrow {
    private final int[] values;
    private final int[] roomPositions;
    private final int hallwaySize;
    private final int roomSize;
    private final List<Integer> legalHallwayPositions;
    private final long[] energyCost;

    public Burrow(IntStream values, IntStream roomPositions, int roomSize) {
        this.values = values.toArray();
        this.roomPositions = roomPositions.toArray();
        this.hallwaySize = this.values.length - this.roomPositions.length * roomSize;
        Set<Integer> roomPositionsSet = Arrays.stream(this.roomPositions).boxed().collect(Collectors.toSet());
        this.legalHallwayPositions = IntStream.range(0, hallwaySize).boxed()
                .filter(not(roomPositionsSet::contains)).toList();
        this.roomSize = roomSize;
        this.energyCost = IntStream.range(0, this.roomPositions.length)
                .mapToLong(i -> LongMath.pow(10L, i)).toArray();
    }

    private Burrow(int[] values, int[] roomPositions, List<Integer> legalHallwayPositions,
            long[] energyCost, int hallwaySize, int roomSize) {
        this.values = values;
        this.roomPositions = roomPositions;
        this.legalHallwayPositions = legalHallwayPositions;
        this.energyCost = energyCost;
        this.hallwaySize = hallwaySize;
        this.roomSize = roomSize;
    }

    public IntStream amphipodsBetween(int from, int to) {
        int min = Math.min(from, to);
        int max = Math.max(from, to);
        return Arrays.stream(values).skip(min).limit(max - min + 1).filter(i -> i >= 0);
    }

    public long getEnergyCost(int amphipod) {
        return energyCost[amphipod];
    }

    public List<Integer> getLegalHallwayPositions() {
        return this.legalHallwayPositions;
    }

    public int getHallwayValue(int index) {
        return this.values[index];
    }

    public int getNumberOfRooms() {
        return roomPositions.length;
    }

    public int getRoomX(int roomIndex) {
        return roomPositions[roomIndex];
    }

    public int getRoomOccupant(int roomIndex, int index) {
        return values[this.hallwaySize + roomIndex + index * roomPositions.length];
    }

    public int getRoomSize() {
        return roomSize;
    }

    public RoomPosition getRoomHead(int roomIndex) {
        int base = hallwaySize + roomIndex;
        int depth = 0;
        RoomPosition head = null;
        for (; depth < roomSize; depth++) {
            int index = base + depth * roomPositions.length;
            if (values[index] != -1) {
                head = new RoomPosition(index, roomPositions[roomIndex], depth, values[index]);
                break;
            }
        }
        for (; depth < roomSize; depth++) {
            int index = base + depth * roomPositions.length;
            if (values[index] != roomIndex) {
                break;
            }
        }

        return head;
    }

    public TargetRoomPosition getTargetPosition(int roomIndex) {
        int base = hallwaySize + roomIndex;
        for (int depth = roomSize - 1; depth >= 0; depth--) {
            int index = base + depth * roomPositions.length;
            if (values[index] == -1) {
                return new TargetRoomPosition(index, roomPositions[roomIndex], depth, energyCost[roomIndex]);
            }
            if (values[index] != roomIndex) {
                return null;
            }
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Burrow burrow = (Burrow) o;
        return Arrays.equals(values, burrow.values);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    public Builder builder() {
        int[] newValues = new int[values.length];
        System.arraycopy(values, 0, newValues, 0, values.length);
        return new Builder(newValues, roomPositions, legalHallwayPositions, energyCost, hallwaySize, roomSize);
    }

    public static class Builder {
        private final int[] values;
        private final int[] roomPositions;
        private final int hallwaySize;
        private final int roomSize;
        private final List<Integer> legalHallwayPositions;
        private final long[] energyCost;

        private Builder(int[] values, int[] roomPositions,
                List<Integer> legalHallwayPositions, long[] energyCost, int hallwaySize, int roomSize) {
            this.values = values;
            this.roomPositions = roomPositions;
            this.legalHallwayPositions = legalHallwayPositions;
            this.energyCost = energyCost;
            this.hallwaySize = hallwaySize;
            this.roomSize = roomSize;
        }

        public Builder setValueAt(int index, int value) {
            values[index] = value;
            return this;
        }

        public Builder deleteValueAt(int index) {
            values[index] = -1;
            return this;
        }

        public Burrow build() {
            return new Burrow(values, roomPositions, legalHallwayPositions, energyCost, hallwaySize, roomSize);
        }
    }
}
