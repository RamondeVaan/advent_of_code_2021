package nl.ramondevaan.aoc2021.day12;

import nl.ramondevaan.aoc2021.util.Parser;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CaveConnectionMapParser implements Parser<List<String>, Map<Cave, Set<Cave>>> {

    private final static Pattern CONNECTION_PATTERN = Pattern.compile("(?<from>\\w+)-(?<to>\\w+)");

    @Override
    public Map<Cave, Set<Cave>> parse(List<String> toParse) {
        return toParse.stream()
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
    }

    private static Connection parseConnection(Matcher matcher) {
        String from = matcher.group("from");
        String to = matcher.group("to");

        return new Connection(
                new Cave(from, StringUtils.isAllUpperCase(from)),
                new Cave(to, StringUtils.isAllUpperCase(to))
        );
    }

    private record Connection(Cave from, Cave to) {
        public Connection reverse() {
            return new Connection(to, from);
        }
    }
}
