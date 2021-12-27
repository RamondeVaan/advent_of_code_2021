package nl.ramondevaan.aoc2021.day23;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Room {
    private final int type;
    private final long energyCost;
    private final List<Amphipod> occupants;
    private final int size;
    private final int x;

    public Room(int type, long energyCost, Stream<Amphipod> stream, int size, int x) {
        this.type = type;
        this.energyCost = energyCost;
        this.occupants = stream.toList();
        this.size = size;
        this.x = x;
    }

    public boolean accepts(Amphipod toCheck) {
        return toCheck.type() == this.type && occupants.size() != size &&
                occupants.stream().noneMatch(amphipod -> amphipod.type() != type);
    }

    public int numberOfOccupants() {
        return occupants.size();
    }

    public List<Amphipod> getOccupants() {
        return occupants;
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
        return occupants.stream().findFirst().orElse(null);
    }

    public Room pop() {
        return new Room(type, energyCost, occupants.stream().skip(1), size, x);
    }

    public Room push(Amphipod amphipod) {
        return new Room(type, energyCost, Stream.concat(Stream.of(amphipod), occupants.stream()), size, x);
    }

    public boolean isValid() {
        return occupants.size() == size && allOccupantsSameAsOwner();
    }

    public boolean allOccupantsSameAsOwner() {
        return occupants.stream().allMatch(amphipod -> amphipod.type() == type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return type == room.type && energyCost == room.energyCost && size == room.size && x == room.x && occupants.equals(
                room.occupants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, energyCost, occupants, size, x);
    }
}
