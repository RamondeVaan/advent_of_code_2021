package nl.ramondevaan.aoc2021.day19;

import nl.ramondevaan.aoc2021.util.Parser;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScannerParser implements Parser<List<String>, Scanner> {

    private final static Pattern SCANNER_PATTERN = Pattern.compile("--- scanner (?<id>\\d+) ---");
    private final static Pattern BEACON_PATTERN = Pattern.compile("(?<x>-?\\d+),(?<y>-?\\d+),(?<z>-?\\d+)");

    @Override
    public Scanner parse(List<String> toParse) {
        return new Scanner(
                parseId(toParse.get(0)),
                new Position(0, 0, 0),
                toParse.stream().skip(1).map(ScannerParser::parseBeacon).toList()
        );
    }

    private static int parseId(String toParse) {
        Matcher matcher = SCANNER_PATTERN.matcher(toParse);

        if (matcher.matches()) {
            return Integer.parseInt(matcher.group("id"));
        }

        throw new IllegalArgumentException();
    }

    private static Position parseBeacon(String toParse) {
        Matcher matcher = BEACON_PATTERN.matcher(toParse);

        if (matcher.matches()) {
            return new Position(
                    Integer.parseInt(matcher.group("x")),
                    Integer.parseInt(matcher.group("y")),
                    Integer.parseInt(matcher.group("z"))
            );
        }

        throw new IllegalArgumentException();
    }
}
