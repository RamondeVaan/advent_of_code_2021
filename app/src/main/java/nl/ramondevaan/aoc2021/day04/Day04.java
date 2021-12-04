package nl.ramondevaan.aoc2021.day04;

import nl.ramondevaan.aoc2021.util.BlankStringPartitioner;
import nl.ramondevaan.aoc2021.util.Partitioner;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Day04 {

    private final List<Integer> numbers;
    private final List<Board> boards;

    public Day04(List<String> lines) {
        NumbersParser numbersParser = new NumbersParser();
        BoardParser boardParser = new BoardParser();
        Partitioner<String> partitioner = new BlankStringPartitioner();

        this.numbers = numbersParser.parse(lines.get(0));
        this.boards = partitioner.partition(lines.subList(2, lines.size())).stream()
                .map(boardParser::parse).toList();
    }

    public long solve1() {
        List<MutableBoard> mutableBoards = getMutableBoards();

        for (int number : numbers) {
            for (MutableBoard board : mutableBoards) {
                board.mark(number);
                if (board.isBingo()) {
                    return (long) board.getUnmarked().sum() * number;
                }
            }
        }

        throw new IllegalStateException();
    }

    public long solve2() {
        List<MutableBoard> activeBoards = getMutableBoards();

        for (int number : numbers) {
            Iterator<MutableBoard> it = activeBoards.listIterator();
            while (it.hasNext()) {
                MutableBoard board = it.next();
                board.mark(number);
                if (board.isBingo()) {
                    it.remove();
                    if (activeBoards.isEmpty()) {
                        return (long) board.getUnmarked().sum() * number;
                    }
                }
            }
        }

        throw new IllegalStateException();
    }

    private List<MutableBoard> getMutableBoards() {
        return this.boards.stream().map(MutableBoard::new).collect(Collectors.toList());
    }
}
