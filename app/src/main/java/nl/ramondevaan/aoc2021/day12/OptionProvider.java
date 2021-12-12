package nl.ramondevaan.aoc2021.day12;

import java.util.stream.Stream;

@FunctionalInterface
public interface OptionProvider {
    Stream<Cave> options(PathState pathState);
}
