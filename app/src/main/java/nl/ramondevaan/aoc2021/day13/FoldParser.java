package nl.ramondevaan.aoc2021.day13;

import nl.ramondevaan.aoc2021.util.Parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FoldParser implements Parser<String, Fold> {

    private final static Pattern FOLD_PATTERN = Pattern.compile("fold along (?<axis>[xy])=(?<value>\\d+)");

    @Override
    public Fold parse(String toParse) {
        Matcher matcher = FOLD_PATTERN.matcher(toParse);
        if (matcher.matches()) {
            return new Fold(
                    Axis.valueOf(matcher.group("axis").toUpperCase()),
                    Integer.parseInt(matcher.group("value"))
            );
        }

        throw new IllegalArgumentException();
    }
}
