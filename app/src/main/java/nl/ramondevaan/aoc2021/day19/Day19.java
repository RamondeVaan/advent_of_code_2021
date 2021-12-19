package nl.ramondevaan.aoc2021.day19;

import nl.ramondevaan.aoc2021.util.BlankStringPartitioner;
import nl.ramondevaan.aoc2021.util.Partitioner;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day19 {

    private final List<List<Beacon>> scanners;

    public Day19(List<String> lines) {
        ScannerParser parser = new ScannerParser();
        Partitioner<String> partitioner = new BlankStringPartitioner();
        this.scanners = partitioner.partition(lines).stream().map(parser::parse).toList();
    }

    public long solve1() {
        List<List<Beacon>> temp = new ArrayList<>(scanners);
        List<Map<Integer, ImmutablePair<Beacon, Beacon>>> distanceMaps = scanners.stream()
                .map(Day19::distanceMap).collect(Collectors.toList());
        List<Beacon> scannerPositions = scanners.stream().map(scanner -> new Beacon(
                0,
                0,
                0
        )).collect(Collectors.toList());

        Map<Integer, Map<Integer, Set<Integer>>> hasMapping = pairs(scanners.size())
                .map(pair -> ImmutableTriple.of(
                        pair.left,
                        pair.right,
                        overlappingDistances(distanceMaps.get(pair.left), distanceMaps.get(pair.right))
                ))
                .filter(triple -> triple.right.size() >= 12)
                .flatMap(triple -> Stream.of(triple, ImmutableTriple.of(triple.middle, triple.left, triple.right)))
                .collect(Collectors.groupingBy(
                        triple -> triple.left,
                        Collectors.toMap(triple -> triple.middle, triple -> triple.right)
                ));
        hasMapping.values().forEach(map -> map.remove(0));

        Set<Integer> todo = Set.of(0);

        while (!todo.isEmpty()) {
            todo = todo.stream().flatMap(index -> {
                Map<Integer, Set<Integer>> pairs = hasMapping.remove(index);
                Map<Integer, ImmutablePair<Beacon, Beacon>> distanceMap = distanceMaps.get(index);
                Set<Integer> next = new HashSet<>();
                pairs.forEach((otherIndex, overlap) -> {
                    Transform transform = Transform.findTransform(
                            getBeacons(distanceMap, overlap),
                            getBeacons(distanceMaps.get(otherIndex), overlap)
                    );

                    if (transform == null) {
                        return;
                    }

                    Beacon newScannerPosition = transform.apply(scannerPositions.get(otherIndex));
                    scannerPositions.set(otherIndex, newScannerPosition);

                    hasMapping.values().forEach(map -> map.remove(otherIndex));
                    next.add(otherIndex);
                    List<Beacon> transformed = temp.get(otherIndex).stream().map(transform::apply).toList();
                    temp.set(otherIndex, transformed);
                    distanceMaps.set(otherIndex, distanceMap(transformed));
                });

                return next.stream();
            }).collect(Collectors.toUnmodifiableSet());
        }

        Set<Beacon> result = temp.stream().flatMap(List::stream).collect(Collectors.toUnmodifiableSet());

        List<Beacon> list = new ArrayList<>(result);
        list.sort(Comparator.comparingInt(Beacon::x).thenComparingInt(Beacon::y).thenComparingInt(Beacon::z));
        String collect = list.stream().map(beacon -> String.format(
                "%d,%d,%d",
                beacon.x(),
                beacon.y(),
                beacon.z()
        )).collect(Collectors.joining("\n"));

        int maxDist = pairs(scannerPositions.size())
                .mapToInt(pair -> manhattanDistance(scannerPositions.get(pair.left), scannerPositions.get(pair.right)))
                .max().orElseThrow();

        return result.size();
    }

    private static Map<Integer, ImmutablePair<Beacon, Beacon>> distanceMap(Collection<Beacon> beacons) {
        List<Beacon> temp = new ArrayList<>(beacons);
        return pairs(beacons.size()).map(pair -> ImmutablePair.of(temp.get(pair.left), temp.get(pair.right)))
                .collect(Collectors.toUnmodifiableMap(pair -> distance(pair.left, pair.right), Function.identity()));
    }

    private static int distance(Beacon left, Beacon right) {
        int xDiff = left.x() - right.x();
        int yDiff = left.y() - right.y();
        int zDiff = left.z() - right.z();

        return xDiff * xDiff + yDiff * yDiff + zDiff * zDiff;
    }

    private static int manhattanDistance(Beacon left, Beacon right) {
        return Math.abs(left.x() - right.x()) +
                Math.abs(left.y() - right.y()) +
                Math.abs(left.z() - right.z());
    }

    private static Set<Integer> overlappingDistances(
            Map<Integer, ImmutablePair<Beacon, Beacon>> leftDistanceMap,
            Map<Integer, ImmutablePair<Beacon, Beacon>> rightDistanceMap) {
        Set<Integer> overlap = new HashSet<>(leftDistanceMap.keySet());
        overlap.retainAll(rightDistanceMap.keySet());

        return overlap;

//        Set<Beacon> left = new HashSet<>();
//        Set<Beacon> right = new HashSet<>();
//
//        overlap.forEach(distance -> {
//            ImmutablePair<Beacon, Beacon> leftBeacons = leftDistanceMap.get(distance);
//            ImmutablePair<Beacon, Beacon> rightBeacons = rightDistanceMap.get(distance);
//            left.add(leftBeacons.getLeft());
//            left.add(leftBeacons.getRight());
//            right.add(rightBeacons.getLeft());
//            right.add(rightBeacons.getRight());
//        });
//
//        return ImmutablePair.of(left, right);
    }

    private Set<Beacon> getBeacons(Map<Integer, ImmutablePair<Beacon, Beacon>> distanceMap, Set<Integer> distances) {
        return distances.stream().map(distanceMap::get)
                .flatMap(pair -> Stream.of(pair.left, pair.right))
                .collect(Collectors.toUnmodifiableSet());
    }

    public long solve2() {
        return 0L;
    }

    private static Stream<ImmutablePair<Integer, Integer>> pairs(int size) {
        return IntStream.range(0, size).boxed().flatMap(left -> IntStream.range(left + 1, size)
                .mapToObj(right -> new ImmutablePair<>(left, right)));
    }
}
