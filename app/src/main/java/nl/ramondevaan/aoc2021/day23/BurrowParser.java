package nl.ramondevaan.aoc2021.day23;

import com.google.common.math.LongMath;
import nl.ramondevaan.aoc2021.util.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class BurrowParser implements Parser<List<String>, Burrow> {

    private static final Pattern ROOM_PATTERN = Pattern.compile("(?<id>\\w)#");

    @Override
    public Burrow parse(List<String> toParse) {
        Stream<Amphipod> amphipods = toParse.get(1).chars()
                .filter(character -> Character.isAlphabetic(character) || character == '.')
                .mapToObj(this::parseAmphipodOrNull);

        List<String> roomLines = toParse.subList(2, toParse.size() - 1);
        int index = 0;
        List<Room> rooms = new ArrayList<>();
        Matcher matcher = ROOM_PATTERN.matcher(toParse.get(2));
        while (matcher.find()) {
            int x = matcher.start();
            rooms.add(new Room(
                    index++,
                    roomLines.stream().map(line -> parseAmphipod(line.charAt(x))),
                    roomLines.size(),
                    x - 1
            ));
        }

        return new Burrow(amphipods, rooms.stream());
    }

    private Amphipod parseAmphipodOrNull(int character) {
        if (character == '.') {
            return null;
        }

        return parseAmphipod(character);
    }

    private Amphipod parseAmphipod(int character) {
        int type = character - 'A';
        return new Amphipod(type, getEnergyCost(type));
    }

    private long getEnergyCost(int type) {
        return LongMath.pow(10L, type);
    }
}
