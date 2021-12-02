package nl.ramondevaan.aoc2021.day03;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day03Test {

    static Day03 day03;

    @BeforeAll
    static void setUp() throws URISyntaxException, IOException {
        Path path = Path.of(Objects.requireNonNull(Day03Test.class.getResource("/input/day_03.txt")).toURI());
        List<String> lines = Files.readAllLines(path);
        day03 = new Day03(lines);
    }

    @Test
    void puzzle1() {
        assertEquals(0L, day03.solve1());
    }

    @Test
    void puzzle2() {
        assertEquals(0L, day03.solve2());
    }

}