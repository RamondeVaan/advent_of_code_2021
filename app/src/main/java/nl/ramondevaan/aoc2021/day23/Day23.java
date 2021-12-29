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
        queue.add(new Entry(burrow, 0L, remainingCostLowerBound(burrow)));

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
        int maxDepth = burrow.getRoomSize() - 1;

        for (int roomIndex = 0; roomIndex < burrow.getNumberOfRooms(); roomIndex++) {
            int depth = maxDepth;
            int roomX = burrow.getRoomX(roomIndex);
            long roomEnergyCost = burrow.getEnergyCost(roomIndex);
            while (depth >= 0 && burrow.getRoomOccupant(roomIndex, depth) == roomIndex) {
                depth--;
            }

            for (; depth >= 0; depth--) {
                int type = burrow.getRoomOccupant(roomIndex, depth);
                if (type == -1) {
                    break;
                }
                int steps = depth + 1 + Math.max(Math.abs(burrow.getRoomX(type) - roomX), 2);
                sum += steps * burrow.getEnergyCost(type); // Move out incorrectly placed amphipod
                sum += (depth + 1) * roomEnergyCost; // Move in correct amphipod
            }

            long freeSpaces = depth + 1;
            sum += freeSpaces * (freeSpaces + 1) / 2 * roomEnergyCost;
        }

        for (int hallwayIndex : burrow.getLegalHallwayPositions()) {
            int type = burrow.getHallwayValue(hallwayIndex);
            if (type < 0) {
                continue;
            }

            sum += Math.abs(hallwayIndex - burrow.getRoomX(type)) * burrow.getEnergyCost(type);
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
            RoomPosition roomPosition = burrow.getRoomHead(roomIndex);
            if (roomPosition == null || roomPosition.value() == roomIndex) {
                continue;
            }
            TargetRoomPosition target = burrow.getTargetPosition(roomPosition.value());
            if (target == null || burrow.amphipodsBetween(roomPosition.x(), target.x()) > 0) {
                continue;
            }

            int distance = roomPosition.depth() + target.depth() + 2 + Math.abs(roomPosition.x() - target.x());
            long cost = distance * target.energyCost();

            Burrow newBurrow = burrow.builder()
                    .deleteValueAt(roomPosition.valueIndex())
                    .setValueAt(target.valueIndex(), roomPosition.value())
                    .build();

            return Optional.of(new Move(newBurrow, cost));
        }

        return Optional.empty();
    }

    private static Optional<Move> getHallwayToRoomMoves(Burrow burrow) {
        for (int hallwayIndex : burrow.getLegalHallwayPositions()) {
            int amphipod = burrow.getHallwayValue(hallwayIndex);
            if (amphipod < 0) {
                continue;
            }
            TargetRoomPosition target = burrow.getTargetPosition(amphipod);
            if (target == null || burrow.amphipodsBetween(hallwayIndex, target.x()) != 1) {
                continue;
            }

            int distance = target.depth() + 1 + Math.abs(target.x() - hallwayIndex);
            long cost = distance * target.energyCost();

            Burrow newBurrow = burrow.builder()
                    .setValueAt(hallwayIndex, -1)
                    .setValueAt(target.valueIndex(), amphipod)
                    .build();

            return Optional.of(new Move(newBurrow, cost));
        }

        return Optional.empty();
    }

    private static List<Move> getRoomToHallwayMoves(Burrow burrow) {
        List<Move> ret = new ArrayList<>();

        for (int roomIndex = 0; roomIndex < burrow.getNumberOfRooms(); roomIndex++) {
            RoomPosition roomPosition = burrow.getRoomHead(roomIndex);
            if (roomPosition == null) {
                continue;
            }
            for (int hallwayIndex : burrow.getLegalHallwayPositions()) {
                if (burrow.amphipodsBetween(roomPosition.x(), hallwayIndex) > 0) {
                    continue;
                }
                int distance = roomPosition.depth() + 1 + Math.abs(roomPosition.x() - hallwayIndex);
                long cost = distance * burrow.getEnergyCost(roomPosition.value());

                Burrow newBurrow = burrow.builder()
                        .setValueAt(hallwayIndex, roomPosition.value())
                        .deleteValueAt(roomPosition.valueIndex())
                        .build();

                ret.add(new Move(newBurrow, cost));
            }
        }

        return Collections.unmodifiableList(ret);
    }

    private record Entry(Burrow burrow, long cost, long lowerBoundFinalCost) {

    }
}
