package nl.ramondevaan.aoc2021.day24;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day24Test {

    static Day24 day24;

    @BeforeAll
    static void setUp() throws URISyntaxException, IOException {
        Path path = Path.of(Objects.requireNonNull(Day24Test.class.getResource("/input/day_24.txt")).toURI());
        List<String> lines = Files.readAllLines(path);
        day24 = new Day24(lines);
    }

    @Test
    void puzzle1() {
        assertEquals(0L, day24.solve1());
    }

    @Test
    void puzzle2() {
        assertEquals(0L, day24.solve2());
    }

}