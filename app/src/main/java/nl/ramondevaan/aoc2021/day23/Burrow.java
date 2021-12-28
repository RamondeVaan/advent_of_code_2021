package nl.ramondevaan.aoc2021.day23;

import com.google.common.math.LongMath;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public class Burrow {
    private final int[] hallway;
    private final Room[] rooms;
    private final List<Integer> legalHallwayPositions;
    private final List<Long> energyCost;
    private final int roomSize;

    public Burrow(IntStream amphipods, Stream<Room> rooms, int maxAmphipod, int roomSize) {
        this.hallway = amphipods.toArray();
        this.rooms = rooms.toArray(Room[]::new);
        Set<Integer> roomPositions = Arrays.stream(this.rooms).map(Room::getX).collect(Collectors.toSet());
        this.legalHallwayPositions = IntStream.range(0, hallway.length).boxed()
                .filter(not(roomPositions::contains)).toList();
        this.energyCost = IntStream.rangeClosed(0, maxAmphipod).mapToLong(i -> LongMath.pow(10L, i))
                .boxed().toList();
        this.roomSize = roomSize;
    }

    private Burrow(int[] hallway, Room[] rooms, List<Integer> legalHallwayPositions, List<Long> energyCost, int roomSize) {
        this.hallway = hallway;
        this.rooms = rooms;
        this.legalHallwayPositions = legalHallwayPositions;
        this.energyCost = energyCost;
        this.roomSize = roomSize;
    }

    public Stream<Integer> amphipodsBetween(int from, int to) {
        int min = Math.min(from, to);
        int max = Math.max(from, to);
        return this.legalHallwayPositions.stream()
                .dropWhile(i -> i < min)
                .takeWhile(i -> i <= max)
                .map(i -> hallway[i])
                .filter(i -> i >= 0);
    }

    public long getEnergyCost(int amphipod) {
        return energyCost.get(amphipod);
    }

    public List<Integer> getLegalHallwayPositions() {
        return this.legalHallwayPositions;
    }

    public int getHallwayValue(int index) {
        return this.hallway[index];
    }

    public int getNumberOfRooms() {
        return rooms.length;
    }

    public int getRoomX(int roomIndex) {
        return rooms[roomIndex].getX();
    }

    public int getRoomOccupant(int roomIndex, int index) {
        return rooms[roomIndex].getOccupant(index);
    }

    public int getRoomSize() {
        return roomSize;
    }

    public int getRoomFreeSpots(int roomIndex) {
        return rooms[roomIndex].getFreeSpots();
    }

    public int getRoomHead(int roomIndex) {
        return rooms[roomIndex].head();
    }

    public boolean roomReady(int roomIndex) {
        return rooms[roomIndex].allOccupantsSameAsOwner();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Burrow burrow = (Burrow) o;
        return Arrays.equals(hallway, burrow.hallway) && Arrays.equals(rooms, burrow.rooms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(hallway), Arrays.hashCode(rooms));
    }

    public Builder builder() {
        int[] newHallway = new int[hallway.length];
        System.arraycopy(hallway, 0, newHallway, 0, hallway.length);
        Room[] newRooms = new Room[rooms.length];
        System.arraycopy(rooms, 0, newRooms, 0, rooms.length);
        return new Builder(newHallway, newRooms, legalHallwayPositions, energyCost, roomSize);
    }

    public static class Builder {
        private final int[] hallway;
        private final Room[] rooms;
        private final List<Integer> legalHallwayPositions;
        private final List<Long> energyCost;
        private final int roomSize;

        private Builder(int[] hallway, Room[] rooms, List<Integer> legalHallwayPositions, List<Long> energyCost, int roomSize) {
            this.hallway = hallway;
            this.rooms = rooms;
            this.legalHallwayPositions = legalHallwayPositions;
            this.energyCost = energyCost;
            this.roomSize = roomSize;
        }

        public Builder setHallway(int index, int value) {
            hallway[index] = value;
            return this;
        }

        public Builder pushToCorrectRoom(int amphipod) {
            rooms[amphipod] = rooms[amphipod].push(amphipod);
            return this;
        }

        public Builder popRoom(int roomIndex) {
            rooms[roomIndex] = rooms[roomIndex].pop();
            return this;
        }

        public Burrow build() {
            return new Burrow(hallway, rooms, legalHallwayPositions, energyCost, roomSize);
        }
    }
}
