package nl.ramondevaan.aoc2021.day09;

import nl.ramondevaan.aoc2021.util.Parser;

import java.util.List;

public class HeightMapParser implements Parser<List<String>, HeightMap> {
    @Override
    public HeightMap parse(List<String> toParse) {
        int height = toParse.size();
        int width = toParse.stream().findAny().orElseThrow().length();

        int[][] heights = new int[height][];

        for(int h = 0; h < toParse.size(); h++) {
            heights[h] = toParse.get(h).chars().map(c -> c - '0').toArray();
        }

        return new HeightMap(height, width, heights);
    }
}
