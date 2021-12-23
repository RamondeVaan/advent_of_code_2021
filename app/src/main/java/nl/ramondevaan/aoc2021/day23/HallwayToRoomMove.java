package nl.ramondevaan.aoc2021.day23;

import java.util.ArrayList;
import java.util.List;

public record HallwayToRoomMove(int hallwayIndex, int roomIndex) implements Move {

    @Override
    public int cost(Burrow burrow) {
        Amphipod amphipod = burrow.getHallway().get(hallwayIndex);
        Room room = burrow.getRooms().get(roomIndex);
        int distance = room.getSize() - room.numberOfOccupants() + Math.abs(room.getX() - hallwayIndex);
        return distance * amphipod.energyCost();
    }

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
