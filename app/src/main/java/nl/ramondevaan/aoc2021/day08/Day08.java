package nl.ramondevaan.aoc2021.day08;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day08 {

    private final Map<Integer, Set<Character>> numberToSegmentMap;
    private final List<NoteEntry> noteEntries;

    public Day08(List<String> lines) {
        DigitParser digitParser = new DigitParser();
        NoteEntryParser noteEntryParser = new NoteEntryParser(digitParser);
        this.noteEntries = lines.stream().map(noteEntryParser::parse).toList();
        this.numberToSegmentMap = Map.of(
                0, Set.of('a', 'b', 'c', 'e', 'f', 'g'),
                1, Set.of('c', 'f'),
                2, Set.of('a', 'c', 'd', 'e', 'g'),
                3, Set.of('a', 'c', 'd', 'f', 'g'),
                4, Set.of('b', 'c', 'd', 'f'),
                5, Set.of('a', 'b', 'd', 'f', 'g'),
                6, Set.of('a', 'b', 'd', 'e', 'f', 'g'),
                7, Set.of('a', 'c', 'f'),
                8, Set.of('a', 'b', 'c', 'd', 'e', 'f', 'g'),
                9, Set.of('a', 'b', 'c', 'd', 'f', 'g')
        );
    }

    public long solve1() {
        Set<Integer> uniqueDigitSizes = this.numberToSegmentMap.values().stream().map(Set::size)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream()
                .filter(entry -> entry.getValue() == 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toUnmodifiableSet());

        return noteEntries.stream().flatMap(NoteEntry::displayStream)
                .map(Digit::getNumberOfWires)
                .filter(uniqueDigitSizes::contains)
                .count();
    }

    public long solve2() {
        Map<Integer, Integer> intersectionCountToNumberMap = this.numberToSegmentMap.entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        entry -> this.numberToSegmentMap.values().stream().mapToInt(set -> {
                                    Set<Character> intersection = new HashSet<>(set);
                                    intersection.retainAll(entry.getValue());
                                    return intersection.size();
                                })
                                .sum(),
                        Map.Entry::getKey
                ));
        if (intersectionCountToNumberMap.values().stream().distinct().count() != intersectionCountToNumberMap.size()) {
            throw new UnsupportedOperationException();
        }
        return noteEntries.stream()
                .mapToLong(noteEntry -> solveNoteEntry(intersectionCountToNumberMap, noteEntry))
                .sum();
    }

    private static long solveNoteEntry(Map<Integer, Integer> intersectionCountToNumberMap, NoteEntry noteEntry) {
        Map<Digit, Integer> digitToNumberMap = noteEntry.digits().stream().collect(Collectors.toUnmodifiableMap(
                Function.identity(),
                digit -> intersectionCountToNumberMap.get(
                        noteEntry.digits().stream()
                                .mapToInt(otherDigit -> intersectionSize(digit.wires(), otherDigit.wires()))
                                .sum()
                )
        ));
        return noteEntry.displayStream().mapToLong(digitToNumberMap::get)
                .reduce(0L, (left, right) -> left * 10 + right);
    }

    public static <T> int intersectionSize(Set<T> a, Set<T> b) {
        Set<T> ret = new HashSet<>(a);
        ret.retainAll(b);
        return ret.size();
    }
}
