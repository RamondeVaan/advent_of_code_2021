package nl.ramondevaan.aoc2021.day22;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Stream;

public class Day22 {

    private final List<Step> steps;

    public Day22(List<String> lines) {
        StepParser parser = new StepParser();
        this.steps = lines.stream().map(parser::parse).toList();
    }

    public long solve1() {
        Range range = new Range(-50, 50);
        Cuboid cuboid = new Cuboid(range, range, range);
        return solve(steps.stream().filter(step -> step.cuboid().containedIn(cuboid)));
    }

    public long solve2() {
        return solve(steps.stream());
    }

    private static long solve(Stream<Step> steps) {
        List<Cuboid> on = new ArrayList<>();

        steps.forEach(step -> {
            ListIterator<Cuboid> iterator = on.listIterator();
            while (iterator.hasNext()) {
                Cuboid currentCuboid = iterator.next();
                currentCuboid.overlap(step.cuboid()).ifPresent(overlap -> {
                    iterator.remove();
                    currentCuboid.without(overlap).forEach(iterator::add);
                });
            }
            if (step.on()) {
                on.add(step.cuboid());
            }
        });

        return on.stream().mapToLong(Cuboid::size).sum();
    }

}
