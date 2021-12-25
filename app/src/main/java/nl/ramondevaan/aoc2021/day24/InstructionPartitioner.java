package nl.ramondevaan.aoc2021.day24;

import nl.ramondevaan.aoc2021.util.Partitioner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class InstructionPartitioner implements Partitioner<Instruction> {
    @Override
    public List<List<Instruction>> partition(List<Instruction> toPartition) {
        List<List<Instruction>> ret = new ArrayList<>();
        List<Instruction> current = null;

        for (Instruction instruction : toPartition) {
            if (instruction instanceof Input) {
                current = new ArrayList<>();
                current.add(instruction);
                ret.add(Collections.unmodifiableList(current));
            } else {
                Objects.requireNonNull(current).add(instruction);
            }
        }

        return Collections.unmodifiableList(ret);
    }
}
