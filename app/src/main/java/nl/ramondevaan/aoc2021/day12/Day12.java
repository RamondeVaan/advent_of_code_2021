package nl.ramondevaan.aoc2021.day12;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

public class Day12 {

    private final static Cave START = new Cave("start", false);
    private final static Cave END = new Cave("end", false);
    private final Map<Cave, Set<Cave>> caveConnectionMap;

    public Day12(List<String> lines) {
        CaveConnectionMapParser parser = new CaveConnectionMapParser();
        this.caveConnectionMap = parser.parse(lines);
    }

    public long solve1() {
        return solve((pathState) -> caveConnectionMap.get(pathState.tail()).stream()
                .filter(option -> option.big() || !pathState.contains(option)));
    }

    public long solve2() {
        Set<Cave> smallCaves = caveConnectionMap.keySet().stream().filter(not(Cave::big))
                .collect(Collectors.toUnmodifiableSet());
        return solve((pathState) -> caveConnectionMap.get(pathState.tail()).stream()
                .filter(option -> option.big()
                        || !pathState.contains(option)
                        || smallCaves.stream().map(pathState::getOccurrences).noneMatch(value -> value >= 2))
                .filter(not(START::equals)));
    }

    private long solve(OptionProvider optionProvider) {
        CountingResultHandler resultHandler = new CountingResultHandler();
        find(new PathStateImpl(START), optionProvider, resultHandler);
        return resultHandler.getCount();
    }

    private static void find(PathState pathState, OptionProvider optionProvider, ResultHandler resultHandler) {
        if (pathState.tail().equals(END)) {
            resultHandler.handle(pathState);
        } else {
            optionProvider.options(pathState).forEach(option -> {
                find(pathState.push(option), optionProvider, resultHandler);
                pathState.pop();
            });
        }
    }
}
