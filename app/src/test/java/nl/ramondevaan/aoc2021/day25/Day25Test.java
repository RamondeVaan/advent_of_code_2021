package nl.ramondevaan.aoc2021.day25;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day25Test {

    static Day25 day25;

    @BeforeAll
    static void setUp() throws URISyntaxException, IOException {
        Path path = Path.of(Objects.requireNonNull(Day25Test.class.getResource("/input/day_25.txt")).toURI());
        List<String> lines = Files.readAllLines(path);
        day25 = new Day25(lines);
    }

    @Test
    void puzzle() {
        assertEquals(353L, day25.solve());
    }

}