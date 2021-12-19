package nl.ramondevaan.aoc2021.day19;

import nl.ramondevaan.aoc2021.util.Parser;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScannerParser implements Parser<List<String>, List<Beacon>> {

    private final static Pattern BEACON_PATTERN = Pattern.compile("(?<x>-?\\d+),(?<y>-?\\d+),(?<z>-?\\d+)");

    @Override
    public List<Beacon> parse(List<String> toParse) {
        return toParse.stream().skip(1).map(ScannerParser::parseBeacon).toList();
    }

    private static Beacon parseBeacon(String toParse) {
        Matcher matcher = BEACON_PATTERN.matcher(toParse);

        if (matcher.matches()) {
            return new Beacon(
                    Integer.parseInt(matcher.group("x")),
                    Integer.parseInt(matcher.group("y")),
                    Integer.parseInt(matcher.group("z"))
            );
        }

        throw new IllegalArgumentException();
    }
}
