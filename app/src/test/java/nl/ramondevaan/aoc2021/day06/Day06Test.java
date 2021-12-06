package nl.ramondevaan.aoc2021.day06;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day06Test {

    static Day06 day06;

    @BeforeAll
    static void setUp() throws URISyntaxException, IOException {
        Path path = Path.of(Objects.requireNonNull(Day06Test.class.getResource("/input/day_06.txt")).toURI());
        List<String> lines = Files.readAllLines(path);
        day06 = new Day06(lines);
    }

    @Test
    void puzzle1() {
        assertEquals(380612L, day06.solve1());
    }

    @Test
    void puzzle2() {
        assertEquals(1710166656900L, day06.solve2());
    }

}