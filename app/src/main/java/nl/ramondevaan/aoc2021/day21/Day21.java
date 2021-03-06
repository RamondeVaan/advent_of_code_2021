package nl.ramondevaan.aoc2021.day21;

import com.google.common.math.IntMath;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day21 {

    private final static int POSITIONS = 10;
    private final static int DIRAC_MAX_SCORE = 21;
    private final static int DETERMINISTIC_MAX_SCORE = 1000;
    private final static int DETERMINISTIC_DIE_SIZE = 100;
    private final static int MAX_GAME_STATES = POSITIONS * POSITIONS * DIRAC_MAX_SCORE * DIRAC_MAX_SCORE;
    private final static int DIE_SIDES = 3;
    private final static int ROLLS = 3;
    private final static List<Throw> THROW_OPTIONS = IntStream.range(0, IntMath.pow(DIE_SIDES, ROLLS))
            .map(i -> IntStream.range(0, ROLLS)
                    .reduce(0, (last, exp) -> last + i / IntMath.pow(DIE_SIDES, exp) % DIE_SIDES + 1))
            .boxed().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet().stream().map(entry -> new Throw(entry.getKey(), entry.getValue())).toList();

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
        for (; Arrays.stream(scores).noneMatch(score -> score >= DETERMINISTIC_MAX_SCORE); turn++) {
            int player = turn % positions.length;
            int totalRoll = 0;
            for (int roll = 0; roll < ROLLS; roll++) {
                totalRoll += die;
                die = (die % DETERMINISTIC_DIE_SIZE) + 1;
            }
            positions[player] = (positions[player] + totalRoll) % POSITIONS;
            scores[player] += positions[player] + 1;
        }

        return turn * 3L * Arrays.stream(scores).min().orElseThrow();
    }

    public long solve2() {
        long[][][][][] allStates = new long[2][POSITIONS][DIRAC_MAX_SCORE][POSITIONS][DIRAC_MAX_SCORE];
        int p1Position = playerPositions.get(0);
        int p2Position = playerPositions.get(1);

        allStates[0][p1Position][0][p2Position][0] = 1L;

        int[][] toCheck = new int[MAX_GAME_STATES][4];
        int[][] nextToCheck = new int[MAX_GAME_STATES][4];
        toCheck[0] = new int[]{p1Position, 0, p2Position, 0};

        long[] playerWins = new long[]{0L, 0L};
        int player = 0;
        int len = 1;

        while (len > 0) {
            int nextLen = 0;
            int nextPlayer = (player + 1) % 2;

            for (int i = 0; i < len; i++) {
                int[] state = toCheck[i];
                long worlds = allStates[player][state[0]][state[1]][state[2]][state[3]];
                if (worlds == 0L) {
                    continue;
                }

                allStates[player][state[0]][state[1]][state[2]][state[3]] = 0;

                for (Throw option : THROW_OPTIONS) {
                    int position = (state[0] + option.value) % 10;
                    int score = state[1] + position + 1;

                    if (score >= DIRAC_MAX_SCORE) {
                        playerWins[player] += worlds * option.multiplier;
                        continue;
                    }

                    allStates[nextPlayer][state[2]][state[3]][position][score] += worlds * option.multiplier;

                    nextToCheck[nextLen][0] = state[2];
                    nextToCheck[nextLen][1] = state[3];
                    nextToCheck[nextLen][2] = position;
                    nextToCheck[nextLen][3] = score;
                    nextLen++;
                }
            }
            len = nextLen;
            int[][] temp = toCheck;
            toCheck = nextToCheck;
            nextToCheck = temp;
            player = nextPlayer;
        }

        return Math.max(playerWins[0], playerWins[1]);
    }

    private record Throw(int value, long multiplier) {

    }
}
