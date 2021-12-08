package nl.ramondevaan.aoc2021.day08;

import nl.ramondevaan.aoc2021.util.Parser;

import java.util.Arrays;
import java.util.List;

public record NoteEntryParser(DigitParser digitParser) implements Parser<String, NoteEntry> {

    @Override
    public NoteEntry parse(String toParse) {
        String[] split = toParse.split("\\|");
        return new NoteEntry(parseDigits(split[0]), parseDigits(split[1]));
    }

    private List<Digit> parseDigits(String toParse) {
        return Arrays.stream(toParse.trim().split("\\s")).map(digitParser::parse).toList();
    }
}
