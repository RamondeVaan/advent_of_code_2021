package nl.ramondevaan.aoc2021.day23;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Burrow {
    private final List<Amphipod> hallway;
    private final List<Room> rooms;

    public Burrow(Stream<Amphipod> amphipods, Stream<Room> rooms) {
        this.hallway = amphipods.toList();
        this.rooms = rooms.toList();
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
}
