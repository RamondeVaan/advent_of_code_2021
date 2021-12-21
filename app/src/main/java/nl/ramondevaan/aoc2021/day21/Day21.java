package nl.ramondevaan.aoc2021.day21;

import com.google.common.math.IntMath;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

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

        return solve3(throwOptions, new GameState(playerPositions.get(0), playerPositions.get(1), 0, 0, true));
    }

    private long solve3(Throw[] throwOptions, GameState gameState) {
        Map<GameState, Long> stateMap = Map.of(gameState, 1L);

        long playerOneWins = 0L;
        long playerTwoWins = 0L;

        while (!stateMap.isEmpty()) {
            Map<GameState, Long> newStateMap = new HashMap<>();

            for (Map.Entry<GameState, Long> entry : stateMap.entrySet()) {
                GameState state = entry.getKey();
                long arity = entry.getValue();
                if (state.playerOneScore() >= 21) {
                    playerOneWins += arity;
                    continue;
                }
                if (state.playerTwoScore() >= 21) {
                    playerTwoWins += arity;
                    continue;
                }
                for (Throw option : throwOptions) {
                    int playerOnePosition, playerTwoPosition, playerOneScore, playerTwoScore;
                    if (state.playerOneTurn()) {
                        playerOnePosition = (state.playerOnePosition() + option.value) % 10;
                        playerTwoPosition = state.playerTwoPosition();
                        playerOneScore = state.playerOneScore() + playerOnePosition + 1;
                        playerTwoScore = state.playerTwoScore();
                    } else {
                        playerOnePosition = state.playerOnePosition();
                        playerTwoPosition = (state.playerTwoPosition() + option.value) % 10;
                        playerOneScore = state.playerOneScore();
                        playerTwoScore = state.playerTwoScore() + playerTwoPosition + 1;
                    }
                    GameState newState = new GameState(
                            playerOnePosition,
                            playerTwoPosition,
                            playerOneScore,
                            playerTwoScore,
                            !state.playerOneTurn()
                    );
                    newStateMap.merge(newState, arity * option.multiplier, Long::sum);
                }
            }

            stateMap.forEach((state, arity) -> {
            });

            stateMap = newStateMap;
        }

        return LongStream.of(playerOneWins, playerTwoWins).max().orElseThrow();
    }

    private record Throw(int value, long multiplier) {

    }
}
