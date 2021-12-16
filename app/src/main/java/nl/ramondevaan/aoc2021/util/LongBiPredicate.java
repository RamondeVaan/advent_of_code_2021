package nl.ramondevaan.aoc2021.util;

@FunctionalInterface
public interface LongBiPredicate {
    boolean test(long left, long right);
}
