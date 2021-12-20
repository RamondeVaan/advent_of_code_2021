package nl.ramondevaan.aoc2021.day19;

import nl.ramondevaan.aoc2021.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static nl.ramondevaan.aoc2021.util.CombinatoricsUtils.pairs;

public class Scanner {

    private final int id;
    private final Position scanner;
    private final List<Position> beacons;
    private final Map<Integer, Pair> distanceMap;

    public Scanner(int id, Position scanner, List<Position> beacons) {
        this.id = id;
        this.scanner = scanner;
        this.beacons = beacons;
        this.distanceMap = pairs(this.beacons.size()).collect(Collectors.toUnmodifiableMap(
                pair -> beacons.get(pair.left()).distanceSquared(beacons.get(pair.right())),
                Function.identity()
        ));
    }

    private Scanner(int id, Position scanner, List<Position> beacons, Map<Integer, Pair> distanceMap) {
        this.id = id;
        this.scanner = scanner;
        this.beacons = beacons;
        this.distanceMap = distanceMap;
    }

    public int getId() {
        return id;
    }

    public Position getScanner() {
        return scanner;
    }

    public List<Position> getBeacons() {
        return beacons;
    }

    public Set<Integer> getDistances() {
        return distanceMap.keySet();
    }

    public Set<Position> getBeaconsWithDistances(Set<Integer> distances) {
        return distances.stream().map(distanceMap::get)
                .flatMap(pair -> pair.stream().mapToObj(beacons::get))
                .collect(Collectors.toUnmodifiableSet());
    }

    public Scanner apply(Transform transform) {
        return new Scanner(
                id,
                transform.apply(scanner),
                beacons.stream().map(transform::apply).toList(),
                distanceMap
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scanner scanner = (Scanner) o;
        return id == scanner.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
