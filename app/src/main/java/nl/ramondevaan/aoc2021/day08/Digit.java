package nl.ramondevaan.aoc2021.day08;

import java.util.Objects;
import java.util.Set;

public record Digit(Set<Character> wires) {

    public int getNumberOfWires() {
        return wires.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Digit digit = (Digit) o;
        return Objects.equals(wires, digit.wires);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wires);
    }
}
