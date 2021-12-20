package nl.ramondevaan.aoc2021.day19;

import nl.ramondevaan.aoc2021.util.BlankStringPartitioner;
import nl.ramondevaan.aoc2021.util.Partitioner;

import java.util.*;

import static nl.ramondevaan.aoc2021.util.CombinatoricsUtils.pairs;
import static org.apache.commons.collections4.SetUtils.intersection;

public class Day19 {

    private final List<Scanner> scanners;

    public Day19(List<String> lines) {
        ScannerParser parser = new ScannerParser();
        Partitioner<String> partitioner = new BlankStringPartitioner();
        this.scanners = solve(partitioner.partition(lines).stream().map(parser::parse).toList());
    }

    private static List<Scanner> solve(List<Scanner> scanners) {
        List<Scanner> remaining = new ArrayList<>(scanners);
        Set<Scanner> checking = Set.of(remaining.remove(remaining.size() - 1));
        List<Scanner> result = new ArrayList<>(scanners.size());
        result.addAll(checking);

        while (!checking.isEmpty()) {
            Set<Scanner> next = new HashSet<>();
            for (Scanner reference : checking) {
                ListIterator<Scanner> iterator = remaining.listIterator();
                while (iterator.hasNext()) {
                    Scanner template = iterator.next();
                    Set<Integer> intersection = intersection(reference.getDistances(), template.getDistances());
                    if (intersection.size() < 66) {
                        continue;
                    }
                    Transform transform = Transform.findTransform(
                            reference.getBeaconsWithDistances(intersection),
                            template.getBeaconsWithDistances(intersection)
                    );

                    if (transform == null) {
                        continue;
                    }

                    iterator.remove();
                    Scanner transformed = template.apply(transform);
                    next.add(transformed);
                    result.add(transformed);
                }
            }
            checking = next;
        }

        return Collections.unmodifiableList(result);
    }

    public long solve1() {
        return this.scanners.stream().flatMap(scanner -> scanner.getBeacons().stream()).distinct().count();
    }

    public long solve2() {
        return pairs(this.scanners.size())
                .mapToInt(pair -> this.scanners.get(pair.left()).getScanner().manhattanDistance(
                        this.scanners.get(pair.right()).getScanner()))
                .max().orElseThrow();
    }
}
