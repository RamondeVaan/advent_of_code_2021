package nl.ramondevaan.aoc2021.day23;

import nl.ramondevaan.aoc2021.util.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class BurrowParser implements Parser<List<String>, Burrow> {

    private static final Pattern ROOM_PATTERN = Pattern.compile("(?<id>\\w)#");

    @Override
    public Burrow parse(List<String> toParse) {
        IntStream amphipods = toParse.get(1).substring(1, toParse.get(1).length() - 1).chars()
                .map(i -> i == '.' ? '@' : i)
                .map(this::parseAmphipod);

        int max = Integer.MIN_VALUE;
        List<String> roomLines = toParse.subList(2, toParse.size() - 1);
        int index = 0;
        List<Room> rooms = new ArrayList<>();
        Matcher matcher = ROOM_PATTERN.matcher(toParse.get(2));
        while (matcher.find()) {
            int x = matcher.start();
            List<Integer> roomAmphipods = roomLines.stream().map(line -> parseAmphipod(line.charAt(x))).toList();
            max = Math.max(max, roomAmphipods.stream().mapToInt(Integer::intValue).max().orElse(Integer.MIN_VALUE));
            rooms.add(new Room(
                    index++,
                    roomAmphipods.stream().mapToInt(Integer::intValue),
                    x - 1
            ));
        }

        return new Burrow(amphipods, rooms.stream(), max, roomLines.size());
    }

    private int parseAmphipod(int character) {
        return character - 'A';
    }
}
