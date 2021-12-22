package nl.ramondevaan.aoc2021.day22;

import nl.ramondevaan.aoc2021.util.Parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StepParser implements Parser<String, Step> {

    private final static Pattern STEP_PATTERN = Pattern.compile("(?<on>on|off) " +
                                                                        "x=(?<xMin>-?\\d+)\\.\\.(?<xMax>-?\\d+)," +
                                                                        "y=(?<yMin>-?\\d+)\\.\\.(?<yMax>-?\\d+)," +
                                                                        "z=(?<zMin>-?\\d+)\\.\\.(?<zMax>-?\\d+)");

    @Override
    public Step parse(String toParse) {
        Matcher matcher = STEP_PATTERN.matcher(toParse);

        if (matcher.matches()) {
            return new Step(
                    matcher.group("on").equals("on"),
                    new Cuboid(
                            new Range(
                                    Integer.parseInt(matcher.group("xMin")),
                                    Integer.parseInt(matcher.group("xMax"))
                            ),
                            new Range(
                                    Integer.parseInt(matcher.group("yMin")),
                                    Integer.parseInt(matcher.group("yMax"))
                            ),
                            new Range(
                                    Integer.parseInt(matcher.group("zMin")),
                                    Integer.parseInt(matcher.group("zMax"))
                            )
                    )
            );
        }

        throw new IllegalArgumentException();
    }
}
