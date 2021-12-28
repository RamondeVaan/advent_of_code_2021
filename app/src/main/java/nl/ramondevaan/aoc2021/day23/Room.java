package nl.ramondevaan.aoc2021.day23;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public class Room {
    private final int type;
    private final long energyCost;
    private final Amphipod[] occupants;
    private final int size;
    private final int x;

    public Room(int type, long energyCost, Stream<Amphipod> stream, int size, int x) {
        this.type = type;
        this.energyCost = energyCost;
        this.occupants = stream.toArray(Amphipod[]::new);
        this.size = size;
        this.x = x;
    }

    public Room(int type, long energyCost, Amphipod[] occupants, int size, int x) {
        this.type = type;
        this.energyCost = energyCost;
        this.occupants = occupants;
        this.size = size;
        this.x = x;
    }

    public boolean accepts(Amphipod toCheck) {
        return toCheck.type() == this.type && occupants.length != size && allOccupantsSameAsOwner();
    }

    public int numberOfOccupants() {
        return occupants.length;
    }

    public Amphipod getOccupant(int index) {
        return occupants[index];
    }

    public int getType() {
        return type;
    }

    public long getEnergyCost() {
        return energyCost;
    }

    public int getX() {
        return x;
    }

    public int getSize() {
        return size;
    }

    public Amphipod head() {
        return occupants.length > 0 ? occupants[0] : null;
    }

    public Room pop() {
        Amphipod[] newOccupants = new Amphipod[occupants.length - 1];
        System.arraycopy(occupants, 1, newOccupants, 0, newOccupants.length);
        return new Room(type, energyCost, newOccupants, size, x);
    }

    public Room push(Amphipod amphipod) {
        Amphipod[] newOccupants = new Amphipod[occupants.length + 1];
        System.arraycopy(occupants, 0, newOccupants, 1, occupants.length);
        newOccupants[0] = amphipod;
        return new Room(type, energyCost, newOccupants, size, x);
    }

    public boolean isValid() {
        return occupants.length == size && allOccupantsSameAsOwner();
    }

    public boolean allOccupantsSameAsOwner() {
        for (Amphipod amphipod : occupants) {
            if (amphipod.type() != type) {
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
        return type == room.type && size == room.size && x == room.x && Arrays.equals(occupants, room.occupants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, Arrays.hashCode(occupants), size, x);
    }
}
