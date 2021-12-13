package nl.ramondevaan.aoc2021.day13;

import nl.ramondevaan.aoc2021.util.BlankStringPartitioner;
import nl.ramondevaan.aoc2021.util.Partitioner;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day13 {

    private final Map<Set<Dot>, String> dotPatternToLetterMap;
    private final Set<Dot> initialDots;
    private final List<Fold> folds;

    public Day13(List<String> lines) {
        Partitioner<String> partitioner = new BlankStringPartitioner();
        DotPatternToLetterParser dotPatternToLetterParser = new DotPatternToLetterParser();
        try {
            Path path = Path.of(Objects.requireNonNull(Day13.class.getResource("/day_13_letters.txt")).toURI());
            this.dotPatternToLetterMap = partitioner.partition(Files.readAllLines(path)).stream()
                    .map(dotPatternToLetterParser::parse).collect(Collectors.toUnmodifiableMap(
                            ImmutablePair::getLeft,
                            ImmutablePair::getRight
                    ));
        } catch (IOException | URISyntaxException e) {
            throw new IllegalStateException();
        }
        DotParser dotParser = new DotParser();
        FoldParser foldParser = new FoldParser();
        List<List<String>> partitions = partitioner.partition(lines);
        this.initialDots = partitions.get(0).stream().map(dotParser::parse).collect(Collectors.toUnmodifiableSet());
        this.folds = partitions.get(1).stream().map(foldParser::parse).toList();
    }

    public long solve1() {
        Set<Dot> dots = new HashSet<>(initialDots);
        Fold fold = folds.stream().findFirst().orElseThrow();
        return dots.stream().map(dot -> dot.fold(fold)).distinct().count();
    }

    public String solve2() {
        Set<Dot> dots = new HashSet<>(initialDots);

        for (Fold fold : folds) {
            dots = dots.stream().map(dot -> dot.fold(fold)).collect(Collectors.toUnmodifiableSet());
        }

        return split(dots).map(dotPatternToLetterMap::get).collect(Collectors.joining());
    }

    private Stream<Set<Dot>> split(Set<Dot> dots) {
        Map<Integer, Set<Dot>> byXCoordinate = dots.stream()
                .collect(Collectors.groupingBy(Dot::x, Collectors.toUnmodifiableSet()));
        IntSummaryStatistics stats = byXCoordinate.keySet().stream().mapToInt(Integer::intValue).summaryStatistics();
        IntStream missing = IntStream.range(stats.getMin() + 1, stats.getMax())
                .filter(index -> !byXCoordinate.containsKey(index))
                .flatMap(i -> IntStream.of(i, i + 1));
        missing = IntStream.concat(IntStream.of(stats.getMin()), missing);
        int[] indices = IntStream.concat(missing, IntStream.of(stats.getMax() + 1)).toArray();
        return IntStream.iterate(0, i -> i < indices.length - 1, i -> i + 2).boxed()
                .map(i -> IntStream.range(indices[i], indices[i + 1]).boxed()
                        .map(byXCoordinate::get).flatMap(Set::stream)
                        .map(dot -> new Dot(dot.x() - indices[i], dot.y()))
                        .collect(Collectors.toUnmodifiableSet()))
                .filter(Predicate.not(Set::isEmpty));
    }
}
