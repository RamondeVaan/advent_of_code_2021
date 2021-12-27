package nl.ramondevaan.aoc2021.day23;

import nl.ramondevaan.aoc2021.day13.Day13;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

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
        return solve(burrow, 0L, Long.MAX_VALUE);
    }

    public long solve2() {
        return solve(extendedBurrow, 0L, Long.MAX_VALUE);
    }

//    private static long solve(Burrow burrow) {
//        // TODO: Put generated states in map with min cost. States can be reached via different paths!
//        // TODO: Make priority queue. Order based on lower bound for cost.
//        // TODO: Compute lower bound for cost can be determined by moving all incorrect Amphipods up,
//        //  move to correct x and move back down
//
//        Map<Burrow, List<? extends Move>> burrowMoves = new HashMap<>();
//        burrowMoves.put(burrow, getMoves(burrow));
//        Map<Burrow, Long> lowestCost = new HashMap<>();
//        lowestCost.put(burrow, 0L);
//        PriorityQueue<Entry> queue = new PriorityQueue<>(Comparator.comparingLong(Entry::lowerBoundFinalCost));
//        queue.add(new Entry(burrow, 0L));
//
//        Entry entry;
//        while ((entry = queue.poll()) != null) {
//            if (entry.burrow.isValid()) {
//                return entry.lowerBoundFinalCost;
//            }
//
//            long cost = lowestCost.get(entry.burrow);
//            List<? extends Move> moves = burrowMoves.get(entry.burrow);
//
//
//        }
//    }

    private static long solve(Burrow burrow, long cost, long min) {
        if (burrow.isValid()) {
            return cost;
        }

        long currentMin = min;

        for (Move move : getMoves(burrow)) {
            long newCost = cost + move.cost();
            if (newCost >= min) {
                continue;
            }
            currentMin = Math.min(solve(move.apply(burrow), newCost, currentMin), currentMin);
        }

        return currentMin;
    }

    private static long remainingCostLowerBound(Burrow burrow) {
        // Cost of moving out of room and above target room
        long sum = 0L;
        sum += burrow.getRooms().stream().mapToLong(room -> {
            long roomSum = 0L;
            int x = room.getX();
            int min = room.getSize() - room.numberOfOccupants() + 1;

            boolean otherTypeEncountered = false;
            for (int depth = room.numberOfOccupants() - 1; depth >= 0; depth--) {
                Amphipod amphipod = room.getOccupants().get(depth);
                if (amphipod.type() == room.getType() && !otherTypeEncountered) {
                    continue;
                }
                otherTypeEncountered = true;
                int steps = min + depth + Math.max(Math.abs(burrow.getRooms().get(amphipod.type()).getX() - x), 2);
                roomSum += steps * amphipod.energyCost();
            }

            return roomSum;
        }).sum();

        // Cost of moving from hallway position to position above target room
        sum += burrow.getLegalHallwayPositions().stream().mapToLong(hallwayIndex -> {
            Amphipod amphipod = burrow.getHallway().get(hallwayIndex);
            if (amphipod == null) {
                return 0L;
            }
            return Math.abs(hallwayIndex - burrow.getRooms().get(amphipod.type()).getX()) * amphipod.energyCost();
        }).sum();

        // Cost of moving amphipods to their target room from the space above it
        sum += burrow.getRooms().stream().mapToLong(room -> {
            long roomSum = 0L;
            int min = room.getSize() - room.numberOfOccupants();

            boolean otherTypeEncountered = false;
            for (int depth = room.numberOfOccupants() - 1; depth >= 0; depth--) {
                if (room.getOccupants().get(depth).type() == room.getType() && !otherTypeEncountered) {
                    continue;
                }
                otherTypeEncountered = true;
                int steps = min + depth;
                roomSum += steps * room.getEnergyCost();
            }

            return roomSum;
        }).sum();

        return sum;
    }

    private static List<? extends Move> getMoves(Burrow burrow) {
        List<? extends Move> moves = getRoomToRoomMoves(burrow).map(List::of).orElseGet(List::of);
        moves = moves.isEmpty() ? getHallwayToRoomMoves(burrow).map(List::of).orElseGet(List::of) : moves;
        moves = moves.isEmpty() ? getRoomToHallwayMoves(burrow).toList() : moves;

        return moves;
    }

    private static Optional<? extends Move> getRoomToRoomMoves(Burrow burrow) {
        return burrow.getRooms().stream().flatMap(room -> {
            Amphipod amphipod = room.head();
            if (amphipod == null || amphipod.type() == room.getType()) {
                return Stream.of();
            }
            Room other = burrow.getRooms().get(amphipod.type());
            if (!other.accepts(amphipod) || burrow.amphipodsBetween(room.getX(), other.getX()).findAny().isPresent()) {
                return Stream.of();
            }

            int distance = room.getSize() - room.numberOfOccupants() + 1 +
                    Math.abs(room.getX() - other.getX()) +
                    other.getSize() - other.numberOfOccupants();
            long cost = distance * amphipod.energyCost();

            return Stream.of(new RoomToRoomMove(room.getType(), other.getType(), cost));
        }).findAny();
    }

    private static Optional<? extends Move> getHallwayToRoomMoves(Burrow burrow) {
        List<Amphipod> hallway = burrow.getHallway();
        List<Room> rooms = burrow.getRooms();
        return burrow.getLegalHallwayPositions().stream().flatMap(hallwayIndex -> {
            Amphipod amphipod = hallway.get(hallwayIndex);
            if (amphipod == null) {
                return Stream.of();
            }
            Room room = rooms.get(amphipod.type());
            if (!room.accepts(amphipod) || burrow.amphipodsBetween(hallwayIndex, room.getX()).count() != 1L) {
                return Stream.of();
            }

            int distance = room.getSize() - room.numberOfOccupants() + Math.abs(room.getX() - hallwayIndex);
            long cost = distance * amphipod.energyCost();

            return Stream.of(new HallwayToRoomMove(hallwayIndex, amphipod.type(), cost));
        }).findAny();
    }

    private static Stream<? extends Move> getRoomToHallwayMoves(Burrow burrow) {
        return burrow.getRooms().stream().filter(not(Room::allOccupantsSameAsOwner))
                .flatMap(room -> burrow.getLegalHallwayPositions().stream()
                        .filter(hallwayIndex -> burrow.amphipodsBetween(room.getX(), hallwayIndex).findAny().isEmpty())
                        .map(hallwayIndex -> {
                            Amphipod amphipod = room.head();
                            int distance = room.getSize() - room.numberOfOccupants() + 1
                                    + Math.abs(room.getX() - hallwayIndex);
                            long cost = distance * amphipod.energyCost();
                            return new RoomToHallwayMove(room.getType(), hallwayIndex, cost);
                        }));
    }

    private record Entry(Burrow burrow, long lowerBoundFinalCost) {

    }
}
