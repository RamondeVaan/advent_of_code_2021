package nl.ramondevaan.aoc2021.day13;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day13Test {

    static Day13 day13;

    @BeforeAll
    static void setUp() throws URISyntaxException, IOException {
        Path path = Path.of(Objects.requireNonNull(Day13Test.class.getResource("/input/day_13.txt")).toURI());
        List<String> lines = Files.readAllLines(path);
        day13 = new Day13(lines);
    }

    @Test
    void puzzle1() {
        assertEquals(0L, day13.solve1());
    }

    @Test
    void puzzle2() {
        assertEquals(0L, day13.solve2());
    }

}