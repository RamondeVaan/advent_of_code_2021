package nl.ramondevaan.aoc2021.day23;

import nl.ramondevaan.aoc2021.util.Parser;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class BurrowParser implements Parser<List<String>, Burrow> {

    private static final Pattern ROOM_PATTERN = Pattern.compile("(?<id>\\w)#");

    @Override
    public Burrow parse(List<String> toParse) {
        int roomSize = toParse.size() - 3;
        IntStream roomPositions = getRoomPositions(toParse.get(2));
        IntStream values = toParse.stream().flatMapToInt(String::chars)
                .filter(i -> Character.isAlphabetic(i) || i == '.')
                .map(i -> i == '.' ? -1 : i - 'A');

        return new Burrow(values, roomPositions, roomSize);
    }

    private IntStream getRoomPositions(String line) {
        IntStream.Builder builder = IntStream.builder();

        Matcher matcher = ROOM_PATTERN.matcher(line);
        while (matcher.find()) {
            builder.add(matcher.start() - 1);
        }

        return builder.build();
    }
}
