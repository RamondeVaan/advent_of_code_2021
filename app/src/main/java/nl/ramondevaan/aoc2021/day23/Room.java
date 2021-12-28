package nl.ramondevaan.aoc2021.day23;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;

public class Room {
    private final int type;
    private final int[] occupants;
    private final int freeSpots;
    private final int x;

    public Room(int type, IntStream stream, int x) {
        this.type = type;
        this.occupants = stream.toArray();
        this.freeSpots = (int) Arrays.stream(occupants).filter(i -> i < 0).count();
        this.x = x;
    }

    private Room(int type, int[] occupants, int x, int freeSpots) {
        this.type = type;
        this.occupants = occupants;
        this.freeSpots = freeSpots;
        this.x = x;
    }

    public int getFreeSpots() {
        return this.freeSpots;
    }

    public int getOccupant(int index) {
        return occupants[index];
    }

    public int getX() {
        return x;
    }

    public int getSize() {
        return occupants.length;
    }

    public int head() {
        return freeSpots < occupants.length ? occupants[freeSpots] : -1;
    }

    public Room pop() {
        int[] newOccupants = new int[occupants.length];
        System.arraycopy(occupants, 0, newOccupants, 0, occupants.length);
        newOccupants[freeSpots] = -1;
        return new Room(type, newOccupants, x, freeSpots + 1);
    }

    public Room push(int amphipod) {
        int[] newOccupants = new int[occupants.length];
        System.arraycopy(occupants, 0, newOccupants, 0, occupants.length);
        newOccupants[freeSpots - 1] = amphipod;
        return new Room(type, newOccupants, x, freeSpots - 1);
    }

    public boolean allOccupantsSameAsOwner() {
        for (int amphipod : occupants) {
            if (amphipod == -1) {
                continue;
            }
            if (amphipod != type) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return type == room.type && x == room.x && Arrays.equals(occupants, room.occupants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, Arrays.hashCode(occupants), x);
    }
}
