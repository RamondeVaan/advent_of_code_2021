package nl.ramondevaan.aoc2021.day08;

import java.util.List;
import java.util.stream.Stream;

public record NoteEntry(List<Digit> digits, List<Digit> display) {
    public Stream<Digit> displayStream() {
        return display.stream();
    }
}
