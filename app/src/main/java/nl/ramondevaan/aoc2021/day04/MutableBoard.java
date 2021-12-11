package nl.ramondevaan.aoc2021.day04;

import nl.ramondevaan.aoc2021.util.Coordinate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MutableBoard {
    private final Map<Integer, Coordinate> numberPositionMap;
    private final AtomicInteger[] rowCounters;
    private final AtomicInteger[] columnCounters;
    private boolean bingo;

    public MutableBoard(Board board) {
        this.numberPositionMap = new HashMap<>(board.getNumberPositionMap());
        this.rowCounters = Stream.generate(AtomicInteger::new)
                .limit(board.getRows())
                .toArray(AtomicInteger[]::new);
        this.columnCounters = Stream.generate(AtomicInteger::new)
                .limit(board.getColumns())
                .toArray(AtomicInteger[]::new);
    }

    public void mark(int number) {
        Optional.ofNullable(numberPositionMap.get(number))
                .ifPresent(position -> {
                    numberPositionMap.remove(number);
                    if (this.rowCounters[position.row()].incrementAndGet() == this.columnCounters.length) {
                        this.bingo = true;
                    }
                    if (this.columnCounters[position.column()].incrementAndGet() == this.rowCounters.length) {
                        this.bingo = true;
                    }
                });
    }

    public boolean isBingo() {
        return this.bingo;
    }

    public IntStream getUnmarked() {
        return numberPositionMap.keySet().stream().mapToInt(Integer::intValue);
    }
}
