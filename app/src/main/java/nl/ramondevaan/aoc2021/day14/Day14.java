package nl.ramondevaan.aoc2021.day14;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day14 {

    private final String polymerTemplate;
    private final Map<String, String> pairToInsertionMap;

    public Day14(List<String> lines) {
        PairToInsertionMapParser parser = new PairToInsertionMapParser();
        this.polymerTemplate = lines.get(0);
        this.pairToInsertionMap = parser.parse(lines.subList(2, lines.size()));
    }

    public long solve1() {
        return solve(10);
    }

    public long solve2() {
        return solve(40);
    }

    private long solve(int numberOfSteps) {
        Map<String, Long> occurrenceMap = IntStream.iterate(0, i -> i < polymerTemplate.length() - 1, i -> i + 1)
                .mapToObj(i -> polymerTemplate.substring(i, i + 2))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        for (int i = 0; i < numberOfSteps; i++) {
            occurrenceMap = occurrenceMap.entrySet().stream()
                    .flatMap(entry -> Optional.ofNullable(pairToInsertionMap.get(entry.getKey()))
                            .map(insert -> Stream.of(
                                    ImmutablePair.of(entry.getKey().charAt(0) + insert, entry.getValue()),
                                    ImmutablePair.of(insert + entry.getKey().charAt(1), entry.getValue())
                            )).orElse(Stream.of(ImmutablePair.of(entry.getKey(), entry.getValue()))))
                    .collect(counting());
        }

        LongSummaryStatistics statistics = occurrenceMap.entrySet().stream()
                .flatMap(entry -> entry.getKey().chars().mapToObj(c -> ImmutablePair.of((char) c, entry.getValue())))
                .collect(counting()).entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue() / 2L + entry.getValue() % 2
                )).values().stream().mapToLong(Long::valueOf).summaryStatistics();

        return statistics.getMax() - statistics.getMin();
    }

    private static <K> Collector<ImmutablePair<K, Long>, ?, Map<K, Long>> counting() {
        return Collectors.toUnmodifiableMap(
                ImmutablePair::getLeft,
                ImmutablePair::getRight,
                Long::sum
        );
    }
}
