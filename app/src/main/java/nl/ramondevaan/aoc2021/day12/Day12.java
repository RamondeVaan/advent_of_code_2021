package nl.ramondevaan.aoc2021.day12;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        return solve((currentCave, caveInPathMap) -> caveConnectionMap.get(currentCave).stream()
                .filter(option -> option.big() || caveInPathMap.get(option) == 0));
    }

    public long solve2() {
        return solve((currentCave, caveInPathMap) -> caveConnectionMap.get(currentCave).stream()
                .filter(option -> option.big()
                        || caveInPathMap.get(option) == 0
                        || caveInPathMap.values().stream().noneMatch(value -> value >= 2))
                .filter(not(START::equals)));
    }

    private long solve(BiFunction<Cave, Map<Cave, Integer>, Stream<Cave>> optionProvider) {
        Map<Cave, Integer> caveInPathMap = caveConnectionMap.keySet().stream().filter(not(Cave::big))
                .collect(Collectors.toMap(Function.identity(), cave -> 0));
        caveInPathMap.put(START, 1);
        Deque<Cave> currentPath = new ArrayDeque<>(List.of(START));
        List<List<Cave>> result = new ArrayList<>();
        find(currentPath, caveInPathMap, result, optionProvider);
        return result.size();
    }

    private static void find(Deque<Cave> currentPath, Map<Cave, Integer> caveInPathMap, List<List<Cave>> result, BiFunction<Cave, Map<Cave, Integer>, Stream<Cave>> optionProvider) {
        Cave currentCave = Optional.ofNullable(currentPath.peek()).orElseThrow();
        if (currentCave.equals(END)) {
            result.add(new ArrayList<>(currentPath));
            return;
        }

        optionProvider.apply(currentCave, caveInPathMap).forEach(option -> {
            currentPath.push(option);
            caveInPathMap.computeIfPresent(option, (cave, occurrences) -> occurrences + 1);
            find(currentPath, caveInPathMap, result, optionProvider);
            caveInPathMap.computeIfPresent(option, (cave, occurrences) -> occurrences - 1);
            currentPath.pop();
        });
    }
}
