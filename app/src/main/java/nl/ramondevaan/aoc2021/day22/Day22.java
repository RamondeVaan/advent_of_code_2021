package nl.ramondevaan.aoc2021.day22;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

public class Day22 {

    private final List<Step> steps;

    public Day22(List<String> lines) {
        StepParser parser = new StepParser();
        this.steps = lines.stream().map(parser::parse).toList();
    }

    public long solve1() {
        Range range = new Range(-50, 50);
        Cuboid cuboid = new Cuboid(range, range, range);
        return solve(steps.stream().filter(step -> step.cuboid().containedIn(cuboid)).toList());
    }

    public long solve2() {
        return solve(steps);
    }

    private static long solve(List<Step> steps) {
        List<Step> result = new ArrayList<>();

        for (Step newStep : steps) {
            List<Step> toCheck = new ArrayList<>(List.of(newStep));
            while (!toCheck.isEmpty()) {
                ListIterator<Step> toCheckIterator = toCheck.listIterator();
                outer:
                while (toCheckIterator.hasNext()) {
                    Step stepToCheck = toCheckIterator.next();
                    toCheckIterator.remove();
                    ListIterator<Step> currentIterator = result.listIterator();
                    while (currentIterator.hasNext()) {
                        Step currentStep = currentIterator.next();
                        Optional<Cuboid> optionalOverlap = currentStep.cuboid().overlap(stepToCheck.cuboid());
                        if (optionalOverlap.isPresent()) {
                            Cuboid overlap = optionalOverlap.get();
                            if (stepToCheck.on() != currentStep.on()) {
                                currentIterator.remove();
                                currentStep.cuboid().without(overlap)
                                        .forEach(cuboid -> currentIterator.add(new Step(currentStep.on(), cuboid)));
                                currentIterator.add(new Step(stepToCheck.on(), overlap));
                            }
                            stepToCheck.cuboid().without(overlap)
                                    .forEach(cuboid -> toCheckIterator.add(new Step(stepToCheck.on(), cuboid)));
                            continue outer;
                        }
                    }
                    result.add(stepToCheck);
                }
            }

        }

        return result.stream().filter(Step::on).map(Step::cuboid).mapToLong(Cuboid::size).sum();
    }

}
