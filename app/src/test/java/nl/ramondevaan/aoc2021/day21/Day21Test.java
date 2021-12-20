package nl.ramondevaan.aoc2021.day21;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day21Test {

    static Day21 day21;

    @BeforeAll
    static void setUp() throws URISyntaxException, IOException {
        Path path = Path.of(Objects.requireNonNull(Day21Test.class.getResource("/input/day_21.txt")).toURI());
        List<String> lines = Files.readAllLines(path);
        day21 = new Day21(lines);
    }

    @Test
    void puzzle1() {
        assertEquals(0L, day21.solve1());
    }

    @Test
    void puzzle2() {
        assertEquals(0L, day21.solve2());
    }

}