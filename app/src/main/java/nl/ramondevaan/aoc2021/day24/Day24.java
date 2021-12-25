package nl.ramondevaan.aoc2021.day24;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.LongUnaryOperator;
import java.util.stream.IntStream;

public class Day24 {

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
        Map<Integer, Stats> last = Map.of(0, new Stats(0L, 0L));

        for (Processor processor : processors) {
            Map<Integer, Stats> next = new HashMap<>(last.size() * 9);

            for (Map.Entry<Integer, Stats> entry : last.entrySet()) {
                int z = entry.getKey();
                IntStream.range(1, 10).forEach(value -> {
                    int result = processor.process(value, z);
                    next.merge(result, entry.getValue().apply(current -> current * 10 + value), Stats::combine);
                });
            }

            last = next;
        }

        return last.get(0);
    }

    public record Stats(long min, long max) {
        public static Stats combine(Stats left, Stats right) {
            return new Stats(Math.min(left.min, right.min), Math.max(left.max, right.max));
        }

        public Stats apply(LongUnaryOperator operator) {
            return new Stats(
                    operator.applyAsLong(min),
                    operator.applyAsLong(max)
            );
        }
    }
}
