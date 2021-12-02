package nl.ramondevaan.aoc2021.day02;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day02Test {

    static Day02 day02;

    @BeforeAll
    static void setUp() throws URISyntaxException, IOException {
        Path path = Path.of(Objects.requireNonNull(Day02Test.class.getResource("/input/day_02.txt")).toURI());
        List<String> lines = Files.readAllLines(path);
        day02 = new Day02(lines);
    }

    @Test
    void puzzle1() {
        assertEquals(1604850L, day02.solve1());
    }

    @Test
    void puzzle2() {
        assertEquals(1685186100L, day02.solve2());
    }

}