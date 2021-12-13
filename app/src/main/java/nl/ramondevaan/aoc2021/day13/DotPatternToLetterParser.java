package nl.ramondevaan.aoc2021.day13;

import nl.ramondevaan.aoc2021.util.Parser;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DotPatternToLetterParser implements Parser<List<String>, ImmutablePair<Set<Dot>, String>> {
    @Override
    public ImmutablePair<Set<Dot>, String> parse(List<String> toParse) {
        String value = toParse.get(0);
        List<String> visual = toParse.subList(1, toParse.size());
        Set<Dot> dots = new HashSet<>();

        for (int y = 0; y < visual.size(); y++) {
            char[] chars = visual.get(y).toCharArray();
            for (int x = 0; x < chars.length; x++) {
                if (!Character.isSpaceChar(chars[x])) {
                    dots.add(new Dot(x, y));
                }
            }
        }

        return ImmutablePair.of(dots, value);
    }
}
