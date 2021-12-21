package nl.ramondevaan.aoc2021.day21;

import nl.ramondevaan.aoc2021.util.Parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerParser implements Parser<String, Integer> {

    private final static Pattern PLAYER_PATTERN = Pattern.compile("Player \\d starting position: (?<position>\\d+)");

    @Override
    public Integer parse(String toParse) {
        Matcher matcher = PLAYER_PATTERN.matcher(toParse);

        if (matcher.matches()) {
            return Integer.parseInt(matcher.group("position")) - 1;
        }

        throw new IllegalArgumentException();
    }
}
