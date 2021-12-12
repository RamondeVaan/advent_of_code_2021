package nl.ramondevaan.aoc2021.day12;

import java.util.concurrent.atomic.AtomicLong;

public class CountingResultHandler implements ResultHandler {
    private final AtomicLong counter;

    public CountingResultHandler() {
        counter = new AtomicLong();
    }

    @Override
    public void handle(PathState result) {
        counter.incrementAndGet();
    }

    public long getCount() {
        return counter.get();
    }
}
