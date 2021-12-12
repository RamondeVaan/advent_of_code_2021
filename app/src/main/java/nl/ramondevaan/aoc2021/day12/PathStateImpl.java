package nl.ramondevaan.aoc2021.day12;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class PathStateImpl implements PathState {

    private final Deque<Cave> stack;
    private final Map<Cave, Integer> occurrences;

    public PathStateImpl(Cave tail) {
        this.stack = new ArrayDeque<>();
        this.stack.push(tail);
        this.occurrences = new HashMap<>();
        this.occurrences.put(tail, 1);
    }

    @Override
    public PathState push(Cave cave) {
        this.occurrences.compute(cave, (key, occurrences) -> occurrences == null ? 1 : occurrences + 1);
        this.stack.push(cave);
        return this;
    }

    @Override
    public void pop() {
        occurrences.computeIfPresent(this.stack.pop(), (cave, occurrences) -> occurrences - 1);
    }

    @Override
    public int getOccurrences(Cave cave) {
        return occurrences.getOrDefault(cave, 0);
    }

    @Override
    public Cave tail() {
        return this.stack.peek();
    }

    @Override
    public boolean contains(Cave cave) {
        return getOccurrences(cave) > 0;
    }
}
