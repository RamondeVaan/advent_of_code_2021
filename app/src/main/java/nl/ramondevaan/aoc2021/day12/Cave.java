package nl.ramondevaan.aoc2021.day12;

import java.util.Objects;
import java.util.Set;

public record Cave(String name, boolean big, Set<Cave> options) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cave cave = (Cave) o;
        return big == cave.big && name.equals(cave.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, big);
    }
}
