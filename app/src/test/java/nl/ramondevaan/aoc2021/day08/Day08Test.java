package nl.ramondevaan.aoc2021.day08;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day08Test {

    static Day08 day08;

    @BeforeAll
    static void setUp() throws URISyntaxException, IOException {
        Path path = Path.of(Objects.requireNonNull(Day08Test.class.getResource("/input/day_08.txt")).toURI());
        List<String> lines = Files.readAllLines(path);
        day08 = new Day08(lines);
    }

    @Test
    void puzzle1() {
        assertEquals(330L, day08.solve1());
    }

    @Test
    void puzzle2() {
        assertEquals(1010472L, day08.solve2());
    }

}