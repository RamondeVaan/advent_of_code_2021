package nl.ramondevaan.aoc2021.day12;

import java.util.List;

@FunctionalInterface
public interface ResultHandler {
    void handle(PathState result);
}
