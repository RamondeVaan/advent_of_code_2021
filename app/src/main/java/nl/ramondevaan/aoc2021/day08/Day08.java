package nl.ramondevaan.aoc2021.day08;

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
        Map<Character, Long> segmentOccurrences = numberToSegmentMap.values().stream().flatMap(Set::stream)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Map<Integer, Integer> idToNumberMap = this.numberToSegmentMap.entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        entry -> generateId(segmentOccurrences, entry.getValue()),
                        Map.Entry::getKey
                ));
        if (idToNumberMap.size() != numberToSegmentMap.size()) {
            throw new UnsupportedOperationException();
        }
        return noteEntries.stream()
                .mapToLong(noteEntry -> solveNoteEntry(idToNumberMap, noteEntry))
                .sum();
    }

    private static long solveNoteEntry(Map<Integer, Integer> numberIdentifiers, NoteEntry noteEntry) {
        Map<Character, Long> segmentOccurrences = noteEntry.digitStream().flatMap(Digit::wireStream)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return noteEntry.displayStream()
                .mapToLong(digit -> numberIdentifiers.get(generateId(segmentOccurrences, digit.wires())))
                .reduce(0L, (left, right) -> left * 10 + right);
    }

    private static int generateId(Map<Character, Long> segmentOccurrences, Set<Character> set) {
        return set.stream().map(segmentOccurrences::get).sorted().toList().hashCode();
    }
}
