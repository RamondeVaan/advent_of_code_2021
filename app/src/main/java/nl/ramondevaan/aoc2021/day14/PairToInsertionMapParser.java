package nl.ramondevaan.aoc2021.day14;

import nl.ramondevaan.aoc2021.util.Parser;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PairToInsertionMapParser implements Parser<List<String>, Map<String, String>> {

    private final static Pattern PAIR_INSERTION_RULE_PATTERN = Pattern.compile("(?<pair>\\w+) -> (?<insertion>\\w+)");

    public Map<String, String> parse(List<String> toParse) {
        return toParse.stream().map(PAIR_INSERTION_RULE_PATTERN::matcher)
                .filter(Matcher::matches)
                .collect(Collectors.toUnmodifiableMap(
                        matcher -> matcher.group("pair"),
                        matcher -> matcher.group("insertion")
                ));
    }
}
