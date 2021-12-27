package nl.ramondevaan.aoc2021.day23;

import java.util.ArrayList;
import java.util.List;

public record RoomToRoomMove(int fromRoomIndex, int toRoomIndex, long cost) implements Move {

    @Override
    public Burrow apply(Burrow burrow) {
        List<Room> rooms = new ArrayList<>(burrow.getRooms());
        Room fromRoom = rooms.get(fromRoomIndex);
        Amphipod amphipod = fromRoom.head();
        Room newFromRoom = fromRoom.pop();
        Room newToRoom = rooms.get(toRoomIndex).push(amphipod);
        rooms.set(fromRoomIndex, newFromRoom);
        rooms.set(toRoomIndex, newToRoom);
        return new Burrow(burrow.getHallway().stream(), rooms.stream());
    }
}
