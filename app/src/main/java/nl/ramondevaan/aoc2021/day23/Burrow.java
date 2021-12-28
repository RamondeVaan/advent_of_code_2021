package nl.ramondevaan.aoc2021.day23;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public class Burrow {
    private final List<Amphipod> hallway;
    private final List<Room> rooms;
    private final List<Integer> legalHallwayPositions;

    public Burrow(Stream<Amphipod> amphipods, Stream<Room> rooms) {
        this.hallway = amphipods.toList();
        this.rooms = rooms.toList();
        Set<Integer> roomPositions = this.rooms.stream().map(Room::getX).collect(Collectors.toSet());
        this.legalHallwayPositions = IntStream.range(0, hallway.size()).boxed()
                .filter(not(roomPositions::contains)).toList();
    }

    private Burrow(List<Amphipod> hallway, List<Room> rooms, List<Integer> legalHallwayPositions) {
        this.hallway = hallway;
        this.rooms = rooms;
        this.legalHallwayPositions = legalHallwayPositions;
    }

    public Stream<Amphipod> amphipodsBetween(int from, int to) {
        int min = Math.min(from, to);
        int max = Math.max(from, to);
        return this.legalHallwayPositions.stream()
                .dropWhile(i -> i < min)
                .takeWhile(i -> i <= max).map(hallway::get)
                .filter(Objects::nonNull);
    }

    public List<Integer> getLegalHallwayPositions() {
        return this.legalHallwayPositions;
    }

    public List<Amphipod> getHallway() {
        return hallway;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public boolean isValid() {
        return hallway.stream().allMatch(Objects::isNull) && rooms.stream().allMatch(Room::isValid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Burrow burrow = (Burrow) o;
        return hallway.equals(burrow.hallway) && rooms.equals(burrow.rooms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hallway, rooms);
    }

    public Builder builder() {
        return new Builder(new ArrayList<>(hallway), new ArrayList<>(rooms), legalHallwayPositions);
    }

    public static class Builder {
        private final List<Amphipod> hallway;
        private final List<Room> rooms;
        private final List<Integer> legalHallwayPositions;

        private Builder(List<Amphipod> hallway, List<Room> rooms, List<Integer> legalHallwayPositions) {
            this.hallway = hallway;
            this.rooms = rooms;
            this.legalHallwayPositions = legalHallwayPositions;
        }

        public Builder setHallway(int index, Amphipod value) {
            hallway.set(index, value);
            return this;
        }

        public Builder setRoom(int index, Room room) {
            rooms.set(index, room);
            return this;
        }

        public Burrow build() {
            return new Burrow(
                    Collections.unmodifiableList(hallway),
                    Collections.unmodifiableList(rooms),
                    legalHallwayPositions
            );
        }
    }
}
