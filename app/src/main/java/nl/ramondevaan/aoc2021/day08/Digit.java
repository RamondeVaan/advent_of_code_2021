package nl.ramondevaan.aoc2021.day08;

import java.util.Set;
import java.util.stream.Stream;

public record Digit(Set<Character> wires) {

    public int getNumberOfWires() {
        return wires.size();
    }

    public Stream<Character> wireStream() {
        return this.wires.stream();
    }
}
