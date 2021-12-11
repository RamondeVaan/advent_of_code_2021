package nl.ramondevaan.aoc2021.day12;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day12Test {

    static Day12 day12;

    @BeforeAll
    static void setUp() throws URISyntaxException, IOException {
        Path path = Path.of(Objects.requireNonNull(Day12Test.class.getResource("/input/day_12.txt")).toURI());
        List<String> lines = Files.readAllLines(path);
        day12 = new Day12(lines);
    }

    @Test
    void puzzle1() {
        assertEquals(0L, day12.solve1());
    }

    @Test
    void puzzle2() {
        assertEquals(0L, day12.solve2());
    }

}