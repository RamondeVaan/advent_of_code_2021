package nl.ramondevaan.aoc2021.day21;

import com.google.common.math.IntMath;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day21 {

    private final List<Integer> playerPositions;

    public Day21(List<String> lines) {
        PlayerParser parser = new PlayerParser();
        this.playerPositions = lines.stream().map(parser::parse).toList();
    }

    public long solve1() {
        int[] positions = this.playerPositions.stream().mapToInt(Integer::intValue).toArray();
        int[] scores = new int[positions.length];
        int die = 1;

        int turn = 0;
        for (; Arrays.stream(scores).noneMatch(score -> score >= 1000); turn++) {
            int player = turn % positions.length;
            int totalRoll = 0;
            for (int roll = 0; roll < 3; roll++) {
                totalRoll += die;
                die = (die % 100) + 1;
            }
            positions[player] = (positions[player] + totalRoll) % 10;
            scores[player] += positions[player] + 1;
        }

        return turn * 3L * Arrays.stream(scores).min().orElseThrow();
    }

    public long solve2() {
        int diceSides = 3;
        int rolls = 3;
        Throw[] throwOptions = IntStream.range(0, IntMath.pow(diceSides, rolls))
                .map(i -> IntStream.range(0, rolls)
                        .reduce(0, (last, exp) -> last + i / IntMath.pow(diceSides, exp) % diceSides + 1))
                .boxed()
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                )).entrySet().stream().map(entry -> new Throw(entry.getKey(), entry.getValue())).toArray(Throw[]::new);

        long[][][][][] intState = new long[2][10][21][10][21];
        int p1Position = playerPositions.get(0);
        int p2Position = playerPositions.get(1);

        intState[0][p1Position][0][p2Position][0] = 1L;

        int[][] toCheck = new int[10 * 21 * 10 * 21][4];
        int[][] nextToCheck = new int[10 * 21 * 10 * 21][4];
        toCheck[0] = new int[]{p1Position, 0, p2Position, 0};

        long[] playerWins = new long[]{0L, 0L};
        int player = 0;
        int len = 1;

        while (len > 0) {
            int nextLen = 0;

            for (int i = 0; i < len; i++) {
                int[] state = toCheck[i];
                long arity = intState[player][state[0]][state[1]][state[2]][state[3]];
                if (arity == 0L) {
                    continue;
                }

                intState[player][state[0]][state[1]][state[2]][state[3]] = 0;

                if (player == 0) {
                    for (Throw option : throwOptions) {
                        int position = (state[0] + option.value) % 10;
                        int score = state[1] + position + 1;

                        if (score >= 21) {
                            playerWins[player] += arity * option.multiplier;
                            continue;
                        }

                        intState[1][position][score][state[2]][state[3]] += arity * option.multiplier;

                        nextToCheck[nextLen][0] = position;
                        nextToCheck[nextLen][1] = score;
                        nextToCheck[nextLen][2] = state[2];
                        nextToCheck[nextLen][3] = state[3];
                        nextLen++;
                    }
                } else {
                    for (Throw option : throwOptions) {
                        int position = (state[2] + option.value) % 10;
                        int score = state[3] + position + 1;

                        if (score >= 21) {
                            playerWins[player] += arity * option.multiplier;
                            continue;
                        }

                        intState[0][state[0]][state[1]][position][score] += arity * option.multiplier;

                        nextToCheck[nextLen][0] = state[0];
                        nextToCheck[nextLen][1] = state[1];
                        nextToCheck[nextLen][2] = position;
                        nextToCheck[nextLen][3] = score;
                        nextLen++;
                    }
                }
            }
            len = nextLen;
            int[][] temp = toCheck;
            toCheck = nextToCheck;
            nextToCheck = temp;
            player = (player + 1) % 2;
        }

        return Math.max(playerWins[0], playerWins[1]);
    }

    private record Throw(int value, long multiplier) {

    }
}
