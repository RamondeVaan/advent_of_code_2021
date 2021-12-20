package nl.ramondevaan.aoc2021.util;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CombinatoricsUtils {
    public static Stream<Pair> pairs(int size) {
        return IntStream.range(0, size).boxed()
                .flatMap(left -> IntStream.range(left + 1, size)
                        .mapToObj(right -> new Pair(left, right)));
    }
}
