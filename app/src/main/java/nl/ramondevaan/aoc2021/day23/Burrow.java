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
    private final int[] numberOfFreeSpots;
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
        this.numberOfFreeSpots = new int[this.roomPositions.length];
        for (int i = hallwaySize + 1; i < this.values.length; i++) {
            if (this.values[i] == -1) {
                numberOfFreeSpots[i % this.roomPositions.length]++;
            }
        }
    }

    private Burrow(int[] values, int[] numberOfFreeSpots, int[] roomPositions, List<Integer> legalHallwayPositions,
            long[] energyCost, int hallwaySize, int roomSize) {
        this.values = values;
        this.roomPositions = roomPositions;
        this.numberOfFreeSpots = numberOfFreeSpots;
        this.legalHallwayPositions = legalHallwayPositions;
        this.energyCost = energyCost;
        this.hallwaySize = hallwaySize;
        this.roomSize = roomSize;
    }

    public Stream<Integer> amphipodsBetween(int from, int to) {
        int min = Math.min(from, to);
        int max = Math.max(from, to);
        return this.legalHallwayPositions.stream()
                .dropWhile(i -> i < min)
                .takeWhile(i -> i <= max)
                .map(i -> values[i])
                .filter(i -> i >= 0);
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

    public int getRoomFreeSpots(int roomIndex) {
        return numberOfFreeSpots[roomIndex];
    }

    public int getRoomHead(int roomIndex) {
        return values[this.hallwaySize + roomIndex + numberOfFreeSpots[roomIndex] * roomPositions.length];
    }

    public boolean roomReady(int roomIndex) {
        int base = hallwaySize + roomIndex;
        for (int i = base; i < values.length; i += roomPositions.length) {
            if (values[i] != -1 && values[i] != roomIndex) {
                return false;
            }
        }

        return true;
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
        int[] newNumberOfFreeSpots = new int[numberOfFreeSpots.length];
        System.arraycopy(numberOfFreeSpots, 0, newNumberOfFreeSpots, 0, numberOfFreeSpots.length);
        return new Builder(newValues, newNumberOfFreeSpots, roomPositions,
                           legalHallwayPositions, energyCost, hallwaySize, roomSize
        );
    }

    public static class Builder {
        private final int[] values;
        private final int[] roomPositions;
        private final int[] numberOfFreeSpots;
        private final int hallwaySize;
        private final int roomSize;
        private final List<Integer> legalHallwayPositions;
        private final long[] energyCost;

        private Builder(int[] values, int[] numberOfFreeSpots, int[] roomPositions,
                List<Integer> legalHallwayPositions, long[] energyCost, int hallwaySize, int roomSize) {
            this.values = values;
            this.roomPositions = roomPositions;
            this.numberOfFreeSpots = numberOfFreeSpots;
            this.legalHallwayPositions = legalHallwayPositions;
            this.energyCost = energyCost;
            this.hallwaySize = hallwaySize;
            this.roomSize = roomSize;
        }

        public Builder setHallway(int index, int value) {
            values[index] = value;
            return this;
        }

        public Builder pushToCorrectRoom(int amphipod) {
            numberOfFreeSpots[amphipod]--;
            values[hallwaySize + amphipod + (numberOfFreeSpots[amphipod]) * roomPositions.length] = amphipod;
            return this;
        }

        public Builder popRoom(int roomIndex) {
            values[hallwaySize + roomIndex + (numberOfFreeSpots[roomIndex]) * roomPositions.length] = -1;
            numberOfFreeSpots[roomIndex]++;
            return this;
        }

        public Burrow build() {
            return new Burrow(values, numberOfFreeSpots, roomPositions,
                              legalHallwayPositions, energyCost, hallwaySize, roomSize
            );
        }
    }
}
