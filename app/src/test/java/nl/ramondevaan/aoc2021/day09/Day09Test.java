package nl.ramondevaan.aoc2021.day09;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day09Test {

    static Day09 day09;

    @BeforeAll
    static void setUp() throws URISyntaxException, IOException {
        Path path = Path.of(Objects.requireNonNull(Day09Test.class.getResource("/input/day_09.txt")).toURI());
        List<String> lines = Files.readAllLines(path);
        day09 = new Day09(lines);
    }

    @Test
    void puzzle1() {
        assertEquals(502L, day09.solve1());
    }

    @Test
    void puzzle2() {
        assertEquals(1330560L, day09.solve2());
    }

}