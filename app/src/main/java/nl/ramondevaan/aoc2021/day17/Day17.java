package nl.ramondevaan.aoc2021.day17;

import nl.ramondevaan.aoc2021.util.Position;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day17 {

    private final TargetArea targetArea;
    private final Set<Position> velocities;

    public Day17(List<String> lines) {
        TargetAreaParser parser = new TargetAreaParser();
        this.targetArea = parser.parse(String.join("", lines));
        this.velocities = possibleVelocities().filter(this::hitsTarget).collect(Collectors.toSet());
    }

    public long solve1() {
        long maxY = velocities.stream().mapToInt(Position::y).filter(y -> y > 0).max().orElse(0);
        return (maxY * maxY + maxY) / 2;
    }

    public long solve2() {
        return velocities.size();
    }

    private Stream<Position> possibleVelocities() {
        IntStream xStream = IntStream.concat(
                IntStream.rangeClosed(compute(Math.max(1, targetArea.xMin())), targetArea.xMax()),
                IntStream.rangeClosed(targetArea.xMin(), compute(Math.min(-1, targetArea.xMax())))
        );

        if (targetArea.xMin() <= 0 && targetArea.xMax() >= 0) {
            xStream = IntStream.concat(IntStream.of(0), xStream);
        }

        int yMax = Math.max(targetArea.yMin() * -1, targetArea.yMax());
        return xStream.boxed().flatMap(x -> IntStream.rangeClosed(targetArea.yMin(), yMax)
                .mapToObj(y -> new Position(x, y)));
    }

    private int compute(int i) {
        return (int) (Math.signum(i) * (Math.sqrt(8 * Math.abs(i) + 1) - 1) / 2);
    }

    private boolean hitsTarget(Position velocity) {
        Position currentVelocity = velocity;
        Position currentPosition = new Position(0, 0);
        Predicate<Integer> xRangePassed = velocity.x() > 0 ? x -> x > targetArea.xMax() : x -> x < targetArea.xMin();

        while (true) {
            currentPosition = currentPosition.add(currentVelocity);

            if (targetArea.contains(currentPosition)) {
                return true;
            } else if (xRangePassed.test(currentPosition.x()) || currentPosition.y() < targetArea.yMin()) {
                return false;
            }

            currentVelocity = new Position(Math.max(0, currentVelocity.x() - 1), currentVelocity.y() - 1);
        }
    }
}
