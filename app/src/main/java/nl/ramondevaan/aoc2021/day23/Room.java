package nl.ramondevaan.aoc2021.day23;

import java.util.List;
import java.util.stream.Stream;

public class Room {
    private final char owner;
    private final List<Amphipod> occupants;
    private final int size;
    private final int x;

    public Room(char owner, Stream<Amphipod> stream, int size, int x) {
        this.owner = owner;
        this.occupants = stream.toList();
        this.size = size;
        this.x = x;
    }

    public boolean accepts(Amphipod toCheck) {
        return toCheck.id() == this.owner && occupants.size() != size &&
                occupants.stream().noneMatch(amphipod -> amphipod.id() != owner);
    }

    public int numberOfOccupants() {
        return occupants.size();
    }

    public List<Amphipod> getOccupants() {
        return occupants;
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
        return new Room(owner, occupants.stream().skip(1), size, x);
    }

    public Room push(Amphipod amphipod) {
        return new Room(owner, Stream.concat(Stream.of(amphipod), occupants.stream()), size, x);
    }

    public boolean isValid() {
        return occupants.size() == size && allOccupantsSameAsOwner();
    }

    public boolean allOccupantsSameAsOwner() {
        return occupants.stream().allMatch(amphipod -> amphipod.id() == owner);
    }
}
