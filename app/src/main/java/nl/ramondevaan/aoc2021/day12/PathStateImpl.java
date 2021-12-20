package nl.ramondevaan.aoc2021.day12;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class PathStateImpl implements PathState {

    private final Deque<Cave> stack;
    private final Map<Cave, Integer> occurrences;
    private boolean smallCaveVisitedTwice;

    public PathStateImpl(Cave tail) {
        this.stack = new ArrayDeque<>();
        this.stack.push(tail);
        this.occurrences = new HashMap<>();
        this.occurrences.put(tail, 1);
    }

    @Override
    public PathState push(Cave cave) {
        int occurrence = this.occurrences.getOrDefault(cave, 0);
        this.occurrences.put(cave, occurrence + 1);
        this.stack.push(cave);
        if (!cave.big() && occurrence == 1) {
            smallCaveVisitedTwice = true;
        }
        return this;
    }

    @Override
    public void pop() {
        Cave removed = this.stack.pop();
        int occurrence = occurrences.get(removed);
        occurrences.put(removed, occurrence - 1);
        if (!removed.big() && occurrence == 2) {
            this.smallCaveVisitedTwice = false;
        }
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

    @Override
    public boolean smallCaveVisitedTwice() {
        return smallCaveVisitedTwice;
    }
}
