package nl.ramondevaan.aoc2021.day04;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day04Test {

    static Day04 day04;

    @BeforeAll
    static void setUp() throws URISyntaxException, IOException {
        Path path = Path.of(Objects.requireNonNull(Day04Test.class.getResource("/input/day_04.txt")).toURI());
        List<String> lines = Files.readAllLines(path);
        day04 = new Day04(lines);
    }

    @Test
    void puzzle1() {
        assertEquals(38594L, day04.solve1());
    }

    @Test
    void puzzle2() {
        assertEquals(21184L, day04.solve2());
    }

}