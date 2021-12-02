package nl.ramondevaan.aoc2021.util;

public interface Parser<T, U> {
    U parse(T toParse);
}
