package nl.ramondevaan.aoc2021.day12;

import nl.ramondevaan.aoc2021.util.Parser;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CaveConnectionMapParser implements Parser<List<String>, Set<Cave>> {

    private final static Pattern CONNECTION_PATTERN = Pattern.compile("(?<from>\\w+)-(?<to>\\w+)");

    @Override
    public Set<Cave> parse(List<String> toParse) {
        Map<String, Set<String>> paths = toParse.stream()
                .map(CONNECTION_PATTERN::matcher)
                .filter(Matcher::matches)
                .map(CaveConnectionMapParser::parseConnection)
                .flatMap(connection -> Stream.of(connection, connection.reverse()))
                .collect(Collectors.groupingBy(
                        Connection::from,
                        Collectors.mapping(
                                Connection::to,
                                Collectors.toUnmodifiableSet()
                        )
                ));

        Map<Cave, Set<Cave>> caveMap = new HashMap<>(paths.keySet().size());
        Map<String, Cave> nameMap = new HashMap<>(paths.keySet().size());

        paths.keySet().forEach(name -> {
            Set<Cave> set = new HashSet<>();
            Set<Cave> unmodifiable = Collections.unmodifiableSet(set);
            Cave cave = new Cave(name, StringUtils.isAllUpperCase(name), unmodifiable);
            caveMap.put(cave, set);
            nameMap.put(name, cave);
        });

        caveMap.forEach((cave, set) -> set.addAll(paths.get(cave.name()).stream().map(nameMap::get).toList()));

        return Collections.unmodifiableSet(caveMap.keySet());
    }

    private static Connection parseConnection(Matcher matcher) {
        return new Connection(matcher.group("from"), matcher.group("to"));
    }

    private record Connection(String from, String to) {
        public Connection reverse() {
            return new Connection(to, from);
        }
    }
}
