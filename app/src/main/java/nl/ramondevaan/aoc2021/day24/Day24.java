package nl.ramondevaan.aoc2021.day24;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day24 {

    private final static int THRESHOLD_DIVISOR = 2;
    private final long min;
    private final long max;

    public Day24(List<String> lines) {
        InstructionParser parser = new InstructionParser();
        List<Instruction> temp = parser.parse(lines);
        int maxRegister = temp.stream().mapToInt(Instruction::register).max().orElseThrow();
        InstructionPartitioner partitioner = new InstructionPartitioner();
        List<List<Instruction>> instructions = partitioner.partition(temp);
        List<Processor> processors = instructions.stream()
                .map(instructionList -> new Processor(instructionList, maxRegister + 1)).toList();
        Stats stats = solve(processors);
        this.min = stats.min;
        this.max = stats.max;
    }

    public long solve1() {
        return max;
    }

    public long solve2() {
        return min;
    }

    private static Stats solve(List<Processor> processors) {
        Map<Long, Stats> last = Map.of(0L, new Stats(0L, 0L));

        for (Processor processor : processors) {
            Map<Long, Stats> next = new HashMap<>(last.size() * 9);

            boolean smaller = false;
            for (Map.Entry<Long, Stats> entry : last.entrySet()) {
                long z = entry.getKey();
                Stats stats = entry.getValue();
                long minBase = stats.min * 10;
                long maxBase = stats.max * 10;
                long threshold = z / THRESHOLD_DIVISOR;

                for (int value = 1; value <= 9; value++) {
                    long result = processor.process(value, z);
                    if (result < z && !smaller) {
                        next = new HashMap<>(last.size() * 9);
                        smaller = true;
                    }
                    if (!smaller || result <= threshold) {
                        next.merge(result, new Stats(minBase + value, maxBase + value), Stats::combine);
                    }
                }
            }

            last = next;
        }

        return last.get(0L);
    }

    private record Stats(long min, long max) {
        public static Stats combine(Stats left, Stats right) {
            return new Stats(Math.min(left.min, right.min), Math.max(left.max, right.max));
        }
    }
}
