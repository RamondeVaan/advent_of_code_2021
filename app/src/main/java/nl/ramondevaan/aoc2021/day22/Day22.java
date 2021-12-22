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
        List<Step> result = new ArrayList<>();

        steps.forEach(newStep -> {
            ListIterator<Step> currentIterator = result.listIterator();
            while (currentIterator.hasNext()) {
                Step currentStep = currentIterator.next();
                currentStep.cuboid().overlap(newStep.cuboid()).ifPresent(overlap -> {
                    currentIterator.remove();
                    currentStep.cuboid().without(overlap)
                            .forEach(cuboid -> currentIterator.add(new Step(currentStep.on(), cuboid)));
                });
            }
            if (newStep.on()) {
                result.add(newStep);
            }
        });

        return result.stream().filter(Step::on).map(Step::cuboid).mapToLong(Cuboid::size).sum();
    }

}
