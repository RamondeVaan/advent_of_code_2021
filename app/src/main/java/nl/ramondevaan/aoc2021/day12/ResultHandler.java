package nl.ramondevaan.aoc2021.day12;

@FunctionalInterface
public interface ResultHandler {
    void handle(PathState result);
}
