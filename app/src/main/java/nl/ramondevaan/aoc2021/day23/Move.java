package nl.ramondevaan.aoc2021.day23;

public interface Move {
    long cost();

    Burrow apply(Burrow burrow);
}
