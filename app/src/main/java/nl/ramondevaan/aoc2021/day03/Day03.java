package nl.ramondevaan.aoc2021.day03;

import java.util.*;
import java.util.function.BiFunction;

public class Day03 {

    private final List<String> lines;

    public Day03(List<String> lines) {
        this.lines = lines;
    }

    public long solve1() {
        int[] ones = ones(lines.listIterator());

        int lenDiv2 = lines.size() / 2;
        int gamma = 0;

        for (int one : ones) {
            gamma <<= 1;
            if (one > lenDiv2) {
                gamma |= 0b1;
            }
        }

        int epsilon = gamma ^ ((~0) >>> (32 - ones.length));
        return (long) gamma * epsilon;
    }

    private static int[] ones(Iterator<String> it) {
        String line = it.next();
        int length = line.length();
        int[] ones = new int[length];

        update(ones, line.toCharArray());

        while (it.hasNext()) {
            update(ones, it.next().toCharArray());
        }

        return ones;
    }

    private static void update(int[] ones, char[] chars) {
        for (int i = 0; i < ones.length; i++) {
            ones[i] = ones[i] + (chars[i] == '1' ? 1 : 0);
        }
    }

    public long solve2() {
        int oxygen = calculate(lines, (zeroes, ones) -> zeroes > ones);
        int co2 = calculate(lines, (zeroes, ones) -> zeroes <= ones);

        return (long) oxygen * co2;
    }

    private static int calculate(Collection<String> lines, BiFunction<Integer, Integer, Boolean> criterion) {
        Set<String> cur = new HashSet<>(lines);
        Set<String> other = new HashSet<>();

        for (int index = 0; cur.size() > 1; index++) {
            Iterator<String> it = cur.iterator();
            while (it.hasNext()) {
                String value = it.next();
                if (value.charAt(index) == '1') {
                    it.remove();
                    other.add(value);
                }
            }
            cur = criterion.apply(cur.size(), other.size()) ? cur : other;
            other = new HashSet<>();
        }

        return cur.stream().findAny().map(val -> Integer.parseUnsignedInt(val, 2)).orElseThrow();
    }
}
