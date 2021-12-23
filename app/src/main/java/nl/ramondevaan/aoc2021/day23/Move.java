package nl.ramondevaan.aoc2021.day23;

public interface Move {
    int cost(Burrow burrow);

    Burrow apply(Burrow burrow);
}
