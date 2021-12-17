package nl.ramondevaan.aoc2021.day17;

import nl.ramondevaan.aoc2021.util.Parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TargetAreaParser implements Parser<String, TargetArea> {

    private static final Pattern TARGET_AREA_PATTERN = Pattern.compile(
            "target area: x=(?<xMin>\\d+)..(?<xMax>\\d+), y=(?<yMin>-?\\d+)..(?<yMax>-?\\d+)");

    @Override
    public TargetArea parse(String toParse) {
        Matcher matcher = TARGET_AREA_PATTERN.matcher(toParse);

        if (matcher.matches()) {
            return new TargetArea(
                    Integer.parseInt(matcher.group("xMin")),
                    Integer.parseInt(matcher.group("xMax")),
                    Integer.parseInt(matcher.group("yMin")),
                    Integer.parseInt(matcher.group("yMax"))
            );
        }

        throw new IllegalStateException();
    }
}
