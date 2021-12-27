package nl.ramondevaan.aoc2021.day23;

import java.util.ArrayList;
import java.util.List;

public record HallwayToRoomMove(int hallwayIndex, int roomIndex, long cost) implements Move {

    @Override
    public Burrow apply(Burrow burrow) {
        List<Amphipod> hallway = new ArrayList<>(burrow.getHallway());
        Amphipod amphipod = hallway.get(hallwayIndex);
        hallway.set(hallwayIndex, null);
        List<Room> rooms = new ArrayList<>(burrow.getRooms());
        Room room = rooms.get(roomIndex);
        Room newRoom = room.push(amphipod);
        rooms.set(roomIndex, newRoom);
        return new Burrow(hallway.stream(), rooms.stream());
    }
}
