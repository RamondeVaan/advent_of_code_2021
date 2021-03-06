package nl.ramondevaan.aoc2021.day12;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.function.Predicate.not;

public class Day12 {

    private final Cave start;
    private final Cave end;

    public Day12(List<String> lines) {
        CaveConnectionMapParser parser = new CaveConnectionMapParser();
        Set<Cave> caves = parser.parse(lines);
        this.start = caves.stream().filter(cave -> cave.name().equals("start")).findAny().orElseThrow();
        this.end = caves.stream().filter(cave -> cave.name().equals("end")).findAny().orElseThrow();
    }

    public long solve1() {
        return solve((pathState) -> pathState.tail().options().stream()
                .filter(option -> option.big() || !pathState.contains(option)));
    }

    public long solve2() {
        return solve((pathState) -> pathState.tail().options().stream()
                .filter(option -> option.big() || !pathState.contains(option) || !pathState.smallCaveVisitedTwice())
                .filter(not(start::equals)));
    }

    private long solve(OptionProvider optionProvider) {
        AtomicLong count = new AtomicLong();
        find(new PathStateImpl(start), optionProvider, result -> count.incrementAndGet());
        return count.get();
    }

    private void find(PathState pathState, OptionProvider optionProvider, ResultHandler resultHandler) {
        if (pathState.tail().equals(end)) {
            resultHandler.handle(pathState);
        } else {
            optionProvider.options(pathState).forEach(option -> {
                find(pathState.push(option), optionProvider, resultHandler);
                pathState.pop();
            });
        }
    }
}
