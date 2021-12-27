package nl.ramondevaan.aoc2021.day23;

import nl.ramondevaan.aoc2021.day13.Day13;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
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
        return solve(burrow);
    }

    public long solve2() {
        return solve(extendedBurrow);
    }

    private static long solve(Burrow burrow) {
        Map<Burrow, Long> lowestCost = new HashMap<>();
        lowestCost.put(burrow, 0L);
        PriorityQueue<Entry> queue = new PriorityQueue<>(Comparator.comparingLong(Entry::lowerBoundFinalCost));
        queue.add(new Entry(burrow, 0L,0L));

        Entry entry;
        while ((entry = queue.poll()) != null) {
            if (entry.cost > lowestCost.get(entry.burrow)) {
                continue;
            }
            if (entry.burrow.isValid()) {
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

        for (Room room : burrow.getRooms()) {
            long free = room.getSize() - room.numberOfOccupants();
            sum += (free * free + free) / 2;
            free *= 2;

            boolean otherTypeEncountered = false;
            for (int depth = room.numberOfOccupants() - 1; depth >= 0; depth--) {
                Amphipod amphipod = room.getOccupants().get(depth);
                if (amphipod.type() == room.getType() && !otherTypeEncountered) {
                    continue;
                }
                otherTypeEncountered = true;
                long steps = free + 1 + 2L * depth +
                        Math.max(Math.abs(burrow.getRooms().get(amphipod.type()).getX() - room.getX()), 2);
                sum += steps * amphipod.energyCost();
            }
        }

        for (int hallwayIndex : burrow.getLegalHallwayPositions()) {
            Amphipod amphipod = burrow.getHallway().get(hallwayIndex);
            if (amphipod == null) {
                continue;
            }

            sum += Math.abs(hallwayIndex - burrow.getRooms().get(amphipod.type()).getX()) * amphipod.energyCost();
        }

        return sum;
    }

    private static List<Move> getMoves(Burrow burrow) {
        List<Move> moves = getRoomToRoomMoves(burrow).map(List::of).orElseGet(List::of);
        moves = moves.isEmpty() ? getHallwayToRoomMoves(burrow).map(List::of).orElseGet(List::of) : moves;
        moves = moves.isEmpty() ? getRoomToHallwayMoves(burrow).toList() : moves;

        return moves;
    }

    private static Optional<Move> getRoomToRoomMoves(Burrow burrow) {
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

            List<Room> newRooms = new ArrayList<>(burrow.getRooms());
            Room newFromRoom = room.pop();
            Room newToRoom = other.push(amphipod);
            newRooms.set(room.getType(), newFromRoom);
            newRooms.set(other.getType(), newToRoom);
            Burrow newBurrow = new Burrow(burrow.getHallway().stream(), newRooms.stream());

            return Stream.of(new Move(newBurrow, cost));
        }).findAny();
    }

    private static Optional<Move> getHallwayToRoomMoves(Burrow burrow) {
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

            List<Amphipod> newHallway = new ArrayList<>(burrow.getHallway());
            newHallway.set(hallwayIndex, null);
            List<Room> newRooms = new ArrayList<>(burrow.getRooms());
            Room newRoom = room.push(amphipod);
            newRooms.set(room.getType(), newRoom);
            Burrow newBurrow = new Burrow(newHallway.stream(), newRooms.stream());

            return Stream.of(new Move(newBurrow, cost));
        }).findAny();
    }

    private static Stream<Move> getRoomToHallwayMoves(Burrow burrow) {
        return burrow.getRooms().stream().filter(not(Room::allOccupantsSameAsOwner))
                .flatMap(room -> burrow.getLegalHallwayPositions().stream()
                        .filter(hallwayIndex -> burrow.amphipodsBetween(room.getX(), hallwayIndex).findAny().isEmpty())
                        .map(hallwayIndex -> {
                            Amphipod amphipod = room.head();
                            int distance = room.getSize() - room.numberOfOccupants() + 1
                                    + Math.abs(room.getX() - hallwayIndex);
                            long cost = distance * amphipod.energyCost();


                            List<Room> newRooms = new ArrayList<>(burrow.getRooms());
                            Room newRoom = room.pop();
                            newRooms.set(room.getType(), newRoom);
                            List<Amphipod> newHallway = new ArrayList<>(burrow.getHallway());
                            newHallway.set(hallwayIndex, amphipod);
                            Burrow newBurrow = new Burrow(newHallway.stream(), newRooms.stream());

                            return new Move(newBurrow, cost);
                        }));
    }

    private record Entry(Burrow burrow, long cost, long lowerBoundFinalCost) {

    }
}
