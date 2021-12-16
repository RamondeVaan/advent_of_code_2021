package nl.ramondevaan.aoc2021.day17;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day17Test {

    static Day17 day17;

    @BeforeAll
    static void setUp() throws URISyntaxException, IOException {
        Path path = Path.of(Objects.requireNonNull(Day17Test.class.getResource("/input/day_17.txt")).toURI());
        List<String> lines = Files.readAllLines(path);
        day17 = new Day17(lines);
    }

    @Test
    void puzzle1() {
        assertEquals(0L, day17.solve1());
    }

    @Test
    void puzzle2() {
        assertEquals(0L, day17.solve2());
    }

}