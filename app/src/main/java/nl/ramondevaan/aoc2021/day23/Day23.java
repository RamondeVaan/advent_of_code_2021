package nl.ramondevaan.aoc2021.day23;

import nl.ramondevaan.aoc2021.day13.Day13;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day23 {

    private final Burrow burrow;
    private final Burrow extendedBurrow;

    public Day23(List<String> lines) {
        BurrowParser burrowParser = new BurrowParser();
        this.burrow = burrowParser.parse(lines);
        List<String> extendedLines = new ArrayList<>(lines.subList(0, 3));
        try {
            Path path = Path.of(Objects.requireNonNull(Day13.class.getResource("/day_23_extended.txt")).toURI());
            extendedLines.addAll(Files.readAllLines(path));
        } catch (IOException | URISyntaxException e) {
            throw new IllegalStateException();
        }
        extendedLines.addAll(lines.subList(3, lines.size()));
        this.extendedBurrow = burrowParser.parse(extendedLines);
    }

    public long solve1() {
        return solve(burrow);
    }

    public long solve2() {
        return solve(extendedBurrow);
    }

    private static long solve(Burrow burrow) {
        Map<Burrow, Long> lowestCost = new HashMap<>();
        lowestCost.put(burrow, 0L);
        PriorityQueue<Entry> queue = new PriorityQueue<>(Comparator.comparingLong(Entry::lowerBoundFinalCost));
        queue.add(new Entry(burrow, 0L, 1L));

        Entry entry;
        while ((entry = queue.poll()) != null) {
            if (entry.cost > lowestCost.get(entry.burrow)) {
                continue;
            }
            if (entry.cost == entry.lowerBoundFinalCost) {
                return entry.lowerBoundFinalCost;
            }

            List<Move> moves = getMoves(entry.burrow);

            for (Move move : moves) {
                Burrow newBurrow = move.burrow();
                long newCost = entry.cost + move.cost();
                if (newCost < lowestCost.getOrDefault(newBurrow, Long.MAX_VALUE)) {
                    lowestCost.put(newBurrow, newCost);
                    queue.add(new Entry(newBurrow, newCost, newCost + remainingCostLowerBound(newBurrow)));
                }
            }
        }

        throw new IllegalStateException();
    }

    private static long remainingCostLowerBound(Burrow burrow) {
        long sum = 0L;
        int[] numberOfMissingAmphipods = new int[burrow.getNumberOfRooms()];

        for (int roomIndex = 0; roomIndex < burrow.getNumberOfRooms(); roomIndex++) {
            int min = burrow.getRoomFreeSpots(roomIndex);
            int depth = burrow.getRoomSize() - 1;
            while (depth >= min && burrow.getRoomOccupant(roomIndex, depth) == roomIndex) {
                depth--;
            }

            for (; depth >= min; depth--) {
                int type = burrow.getRoomOccupant(roomIndex, depth);
                numberOfMissingAmphipods[type]++;
                int steps = depth + 1 + Math.max(Math.abs(burrow.getRoomX(type) - burrow.getRoomX(roomIndex)), 2);
                sum += steps * burrow.getEnergyCost(type);
            }
        }

        for (int hallwayIndex : burrow.getLegalHallwayPositions()) {
            int type = burrow.getHallwayValue(hallwayIndex);
            if (type < 0) {
                continue;
            }
            numberOfMissingAmphipods[type]++;

            sum += Math.abs(hallwayIndex - burrow.getRoomX(type)) * burrow.getEnergyCost(type);
        }

        for (int roomIndex = 0; roomIndex < numberOfMissingAmphipods.length; roomIndex++) {
            sum += (long) numberOfMissingAmphipods[roomIndex] * (numberOfMissingAmphipods[roomIndex] + 1) / 2 *
                    burrow.getEnergyCost(roomIndex);
        }

        return sum;
    }

    private static List<Move> getMoves(Burrow burrow) {
        List<Move> moves = getRoomToRoomMoves(burrow).map(List::of).orElseGet(List::of);
        moves = moves.isEmpty() ? getHallwayToRoomMoves(burrow).map(List::of).orElseGet(List::of) : moves;
        moves = moves.isEmpty() ? getRoomToHallwayMoves(burrow) : moves;

        return moves;
    }

    private static Optional<Move> getRoomToRoomMoves(Burrow burrow) {
        for (int roomIndex = 0; roomIndex < burrow.getNumberOfRooms(); roomIndex++) {
            if (burrow.getRoomFreeSpots(roomIndex) == burrow.getRoomSize()) {
                continue;
            }
            int amphipod = burrow.getRoomHead(roomIndex);
            if (amphipod == roomIndex) {
                continue;
            }
            if (!burrow.roomReady(amphipod) ||
                    burrow.amphipodsBetween(
                            burrow.getRoomX(roomIndex),
                            burrow.getRoomX(amphipod)
                    ).findAny().isPresent()) {
                continue;
            }

            int distance = burrow.getRoomFreeSpots(roomIndex) + 1 + burrow.getRoomFreeSpots(amphipod) +
                    Math.abs(burrow.getRoomX(roomIndex) - burrow.getRoomX(amphipod));
            long cost = distance * burrow.getEnergyCost(amphipod);

            Burrow newBurrow = burrow.builder()
                    .popRoom(roomIndex)
                    .pushToCorrectRoom(amphipod)
                    .build();

            return Optional.of(new Move(newBurrow, cost));
        }

        return Optional.empty();
    }

    private static Optional<Move> getHallwayToRoomMoves(Burrow burrow) {
        for (int hallwayIndex : burrow.getLegalHallwayPositions()) {
            int amphipod = burrow.getHallwayValue(hallwayIndex);
            if (amphipod < 0 || !burrow.roomReady(amphipod) ||
                    burrow.amphipodsBetween(hallwayIndex, burrow.getRoomX(amphipod)).count() != 1L) {
                continue;
            }

            int distance = burrow.getRoomFreeSpots(amphipod) + Math.abs(burrow.getRoomX(amphipod) - hallwayIndex);
            long cost = distance * burrow.getEnergyCost(amphipod);

            Burrow newBurrow = burrow.builder()
                    .setHallway(hallwayIndex, -1)
                    .pushToCorrectRoom(amphipod)
                    .build();

            return Optional.of(new Move(newBurrow, cost));
        }

        return Optional.empty();
    }

    private static List<Move> getRoomToHallwayMoves(Burrow burrow) {
        List<Move> ret = new ArrayList<>();

        for (int roomIndex = 0; roomIndex < burrow.getNumberOfRooms(); roomIndex++) {
            if (burrow.getRoomFreeSpots(roomIndex) == burrow.getRoomSize() || burrow.roomReady(roomIndex)) {
                continue;
            }
            int amphipod = burrow.getRoomHead(roomIndex);
            for (int hallwayIndex : burrow.getLegalHallwayPositions()) {
                if (burrow.amphipodsBetween(burrow.getRoomX(roomIndex), hallwayIndex).findAny().isPresent()) {
                    continue;
                }
                int distance = burrow.getRoomFreeSpots(roomIndex) + 1 +
                        Math.abs(burrow.getRoomX(roomIndex) - hallwayIndex);
                long cost = distance * burrow.getEnergyCost(amphipod);

                Burrow newBurrow = burrow.builder()
                        .setHallway(hallwayIndex, amphipod)
                        .popRoom(roomIndex)
                        .build();

                ret.add(new Move(newBurrow, cost));
            }
        }

        return Collections.unmodifiableList(ret);
    }

    private record Entry(Burrow burrow, long cost, long lowerBoundFinalCost) {

    }
}
