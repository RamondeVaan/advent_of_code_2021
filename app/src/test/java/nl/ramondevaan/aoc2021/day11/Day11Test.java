package nl.ramondevaan.aoc2021.day11;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day11Test {

    static Day11 day11;

    @BeforeAll
    static void setUp() throws URISyntaxException, IOException {
        Path path = Path.of(Objects.requireNonNull(Day11Test.class.getResource("/input/day_11.txt")).toURI());
        List<String> lines = Files.readAllLines(path);
        day11 = new Day11(lines);
    }

    @Test
    void puzzle1() {
        assertEquals(1661L, day11.solve1());
    }

    @Test
    void puzzle2() {
        assertEquals(334L, day11.solve2());
    }

}