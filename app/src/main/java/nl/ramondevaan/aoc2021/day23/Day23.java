package nl.ramondevaan.aoc2021.day23;

import nl.ramondevaan.aoc2021.day13.Day13;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

    private static long solve(Burrow burrow, long cost, long min) {
        if (burrow.isValid()) {
            return cost;
        }

        long currentMin = min;

        List<? extends Move> moves = getHallwayToRoomMoves(burrow).toList();
        if (moves.isEmpty()) {
            moves = getRoomToHallwayMoves(burrow).toList();
        }
        for (Move move : moves) {
            long newCost = cost + move.cost(burrow);
            if (newCost >= min) {
                continue;
            }
            currentMin = Math.min(solve(move.apply(burrow), newCost, currentMin), currentMin);
        }

        return currentMin;
    }

    private static Stream<HallwayToRoomMove> getHallwayToRoomMoves(Burrow burrow) {
        List<Amphipod> hallway = burrow.getHallway();
        List<Room> rooms = burrow.getRooms();
        return IntStream.range(0, hallway.size()).boxed().flatMap(hallwayIndex -> {
            Amphipod amphipod = hallway.get(hallwayIndex);
            if (amphipod == null) {
                return Stream.of();
            }
            return IntStream.range(0, rooms.size()).filter(roomIndex -> {
                Room room = rooms.get(roomIndex);
                if (!room.accepts(amphipod)) {
                    return false;
                }

                int dx = Integer.signum(room.getX() - hallwayIndex);
                int from = hallwayIndex + dx;
                return IntStream.rangeClosed(Math.min(from, room.getX()), Math.max(from, room.getX()))
                        .allMatch(index -> hallway.get(index) == null);
            }).mapToObj(roomIndex -> new HallwayToRoomMove(hallwayIndex, roomIndex));
        });
    }

    private static Stream<RoomToHallwayMove> getRoomToHallwayMoves(Burrow burrow) {
        List<Room> rooms = burrow.getRooms();
        List<Amphipod> hallway = burrow.getHallway();
        Set<Integer> illegalXCoordinates = rooms.stream().map(Room::getX).collect(Collectors.toUnmodifiableSet());
        return IntStream.range(0, rooms.size()).boxed().flatMap(roomIndex -> {
            Room room = rooms.get(roomIndex);
            if (room.allOccupantsSameAsOwner()) {
                return Stream.of();
            }
            return IntStream.range(0, hallway.size()).filter(hallwayIndex -> {
                if (hallway.get(hallwayIndex) != null || illegalXCoordinates.contains(hallwayIndex)) {
                    return false;
                }

                int dx = Integer.signum(hallwayIndex - room.getX());
                int from = room.getX() + dx;
                return IntStream.rangeClosed(Math.min(from, hallwayIndex), Math.max(from, hallwayIndex))
                        .allMatch(index -> hallway.get(index) == null);
            }).mapToObj(hallwayIndex -> new RoomToHallwayMove(roomIndex, hallwayIndex));
        });
    }
}
