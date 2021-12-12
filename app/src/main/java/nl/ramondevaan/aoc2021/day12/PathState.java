package nl.ramondevaan.aoc2021.day12;

public interface PathState {

    PathState push(Cave cave);

    void pop();

    int getOccurrences(Cave cave);

    Cave tail();

    boolean contains(Cave cave);
}
