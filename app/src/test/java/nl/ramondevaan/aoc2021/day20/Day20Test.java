package nl.ramondevaan.aoc2021.day20;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day20Test {

    static Day20 day20;

    @BeforeAll
    static void setUp() throws URISyntaxException, IOException {
        Path path = Path.of(Objects.requireNonNull(Day20Test.class.getResource("/input/day_20.txt")).toURI());
        List<String> lines = Files.readAllLines(path);
        day20 = new Day20(lines);
    }

    @Test
    void puzzle1() {
        assertEquals(5489L, day20.solve1());
    }

    @Test
    void puzzle2() {
        assertEquals(19066L, day20.solve2());
    }

}