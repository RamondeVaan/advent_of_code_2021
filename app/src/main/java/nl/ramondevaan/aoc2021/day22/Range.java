package nl.ramondevaan.aoc2021.day22;

import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public record Range(int min, int max) {

    public boolean containedIn(Range range) {
        return min >= range.min && max <= range.max;
    }

    public Stream<Range> without(Range range) {
        Stream<Range> stream;

        if (min < range.min) {
            stream = Stream.of(new Range(min, range.min - 1));
        } else {
            stream = Stream.of();
        }

        if (max > range.max) {
            stream = Stream.concat(stream, Stream.of(new Range(range.max + 1, max)));
        }

        return stream;
    }

    public Optional<Range> overlap(Range range) {
        int newMin = Math.max(min, range.min);
        int newMax = Math.min(max, range.max);
        if (newMin > newMax) {
            return Optional.empty();
        }
        return Optional.of(new Range(newMin, newMax));
    }

    public int size() {
        return max - min + 1;
    }

    public IntStream stream() {
        return IntStream.rangeClosed(min, max);
    }
}
