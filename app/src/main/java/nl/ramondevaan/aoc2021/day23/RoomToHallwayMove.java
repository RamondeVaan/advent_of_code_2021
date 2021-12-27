package nl.ramondevaan.aoc2021.day23;

import java.util.ArrayList;
import java.util.List;

public record RoomToHallwayMove(int roomIndex, int hallwayIndex, long cost) implements Move {

    @Override
    public Burrow apply(Burrow burrow) {
        List<Room> rooms = new ArrayList<>(burrow.getRooms());
        Room room = rooms.get(roomIndex);
        Amphipod amphipod = room.head();
        Room newRoom = room.pop();
        rooms.set(roomIndex, newRoom);
        List<Amphipod> hallway = new ArrayList<>(burrow.getHallway());
        hallway.set(hallwayIndex, amphipod);
        return new Burrow(hallway.stream(), rooms.stream());
    }
}
