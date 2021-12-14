package nl.ramondevaan.aoc2021.day14;

import nl.ramondevaan.aoc2021.util.Parser;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PairToInsertionMapParser implements Parser<List<String>, Map<Pair, Character>> {

    private final static Pattern PAIR_INSERTION_RULE_PATTERN = Pattern.compile(
            "(?<first>\\w)(?<second>\\w) -> (?<insertion>\\w+)");

    public Map<Pair, Character> parse(List<String> toParse) {
        return toParse.stream().map(PAIR_INSERTION_RULE_PATTERN::matcher)
                .filter(Matcher::matches)
                .collect(Collectors.toUnmodifiableMap(
                        matcher -> new Pair(matcher.group("first").charAt(0), matcher.group("second").charAt(0)),
                        matcher -> matcher.group("insertion").charAt(0)
                ));
    }
}
