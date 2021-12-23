package nl.ramondevaan.aoc2021.day23;

import java.util.ArrayList;
import java.util.List;

public record RoomToHallwayMove(int roomIndex, int hallwayIndex) implements Move {

    @Override
    public int cost(Burrow burrow) {
        Room room = burrow.getRooms().get(roomIndex);
        Amphipod amphipod = room.head();
        int distance = room.getSize() - room.numberOfOccupants() + 1 + Math.abs(room.getX() - hallwayIndex);
        return distance * amphipod.energyCost();
    }

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
