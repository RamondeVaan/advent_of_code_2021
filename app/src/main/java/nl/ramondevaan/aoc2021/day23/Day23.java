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
        queue.add(new Entry(burrow, 0L, 0L));

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
            int min = room.getSize() - room.numberOfOccupants() + 1;

            boolean otherTypeEncountered = false;
            for (int depth = room.numberOfOccupants() - 1; depth >= 0; depth--) {
                Amphipod amphipod = room.getOccupant(depth);
                if (amphipod.type() == room.getType() && !otherTypeEncountered) {
                    continue;
                }
                otherTypeEncountered = true;
                int steps = min + depth +
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

        for (Room room : burrow.getRooms()) {
            long fillToDepth = room.getSize() - room.numberOfOccupants();

            for (int depth = room.numberOfOccupants() - 1; depth >= 0; depth--) {
                Amphipod amphipod = room.getOccupant(depth);
                if (amphipod == null || amphipod.type() != room.getType()) {
                    fillToDepth += depth;
                    break;
                }
            }

            sum += ((fillToDepth * fillToDepth + fillToDepth) / 2) * room.getEnergyCost();
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
        for (Room room : burrow.getRooms()) {
            Amphipod amphipod = room.head();
            if (amphipod == null || amphipod.type() == room.getType()) {
                continue;
            }
            Room other = burrow.getRooms().get(amphipod.type());
            if (!other.accepts(amphipod) || burrow.amphipodsBetween(room.getX(), other.getX()).findAny().isPresent()) {
                continue;
            }

            int distance = room.getSize() - room.numberOfOccupants() + 1 +
                    Math.abs(room.getX() - other.getX()) +
                    other.getSize() - other.numberOfOccupants();
            long cost = distance * amphipod.energyCost();

            Burrow newBurrow = burrow.builder()
                    .setRoom(room.getType(), room.pop())
                    .setRoom(other.getType(), other.push(amphipod))
                    .build();

            return Optional.of(new Move(newBurrow, cost));
        }

        return Optional.empty();
    }

    private static Optional<Move> getHallwayToRoomMoves(Burrow burrow) {
        List<Amphipod> hallway = burrow.getHallway();
        List<Room> rooms = burrow.getRooms();

        for (int hallwayIndex : burrow.getLegalHallwayPositions()) {
            Amphipod amphipod = hallway.get(hallwayIndex);
            if (amphipod == null) {
                continue;
            }
            Room room = rooms.get(amphipod.type());
            if (!room.accepts(amphipod) || burrow.amphipodsBetween(hallwayIndex, room.getX()).count() != 1L) {
                continue;
            }

            int distance = room.getSize() - room.numberOfOccupants() + Math.abs(room.getX() - hallwayIndex);
            long cost = distance * amphipod.energyCost();

            Burrow newBurrow = burrow.builder()
                    .setHallway(hallwayIndex, null)
                    .setRoom(room.getType(), room.push(amphipod))
                    .build();

            return Optional.of(new Move(newBurrow, cost));
        }

        return Optional.empty();
    }

    private static List<Move> getRoomToHallwayMoves(Burrow burrow) {
        List<Move> ret = new ArrayList<>();

        for (Room room : burrow.getRooms()) {
            Amphipod amphipod = room.head();
            if (room.allOccupantsSameAsOwner() || amphipod == null) {
                continue;
            }
            for (int hallwayIndex : burrow.getLegalHallwayPositions()) {
                if (burrow.amphipodsBetween(room.getX(), hallwayIndex).findAny().isPresent()) {
                    continue;
                }
                int distance = room.getSize() - room.numberOfOccupants() + 1 + Math.abs(room.getX() - hallwayIndex);
                long cost = distance * amphipod.energyCost();

                Burrow newBurrow = burrow.builder()
                        .setHallway(hallwayIndex, amphipod)
                        .setRoom(room.getType(), room.pop())
                        .build();

                ret.add(new Move(newBurrow, cost));
            }
        }

        return Collections.unmodifiableList(ret);
    }

    private record Entry(Burrow burrow, long cost, long lowerBoundFinalCost) {

    }
}
