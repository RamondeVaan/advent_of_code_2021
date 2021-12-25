package nl.ramondevaan.aoc2021.day24;

import java.util.List;
import java.util.function.LongBinaryOperator;

import static nl.ramondevaan.aoc2021.day24.MathInstruction.*;

public class Processor {

    private final Input inputInstruction;
    private final List<Instruction> instructions;
    private final int numberOfRegisters;

    public Processor(List<Instruction> instructions, int numberOfRegisters) {
        if (instructions.get(0) instanceof Input inputInstruction) {
            this.inputInstruction = inputInstruction;
        } else {
            throw new IllegalArgumentException();
        }
        this.instructions = instructions.subList(1, instructions.size());
        this.numberOfRegisters = numberOfRegisters;
    }

    public long process(int input, long lastRegisterValue) {
        long[] registers = new long[numberOfRegisters];
        registers[inputInstruction.register()] = input;
        registers[registers.length - 1] = lastRegisterValue;
        for (Instruction instruction : instructions) {
            processInstruction(registers, instruction);
        }
        return registers[registers.length - 1];
    }

    private static void processInstruction(long[] registers, Instruction instruction) {
        int registerIndex = instruction.register();
        if (instruction instanceof MathInstruction mathInstruction) {
            long registerValue = registers[registerIndex];
            LongBinaryOperator operator = switch (mathInstruction.operation()) {
                case ADD -> Long::sum;
                case MULTIPLY -> (left, right) -> left * right;
                case DIVIDE -> (left, right) -> left / right;
                case MODULO -> (left, right) -> left % right;
                case EQUAL -> (left, right) -> left == right ? 1 : 0;
                default -> throw new UnsupportedOperationException();
            };

            long otherValue;
            if (instruction instanceof ConstantInstruction constantInstruction) {
                otherValue = constantInstruction.value();
            } else if (instruction instanceof RegisterInstruction registerInstruction) {
                int otherRegisterIndex = registerInstruction.otherRegister();
                otherValue = registers[otherRegisterIndex];
            } else {
                throw new UnsupportedOperationException();
            }

            registers[registerIndex] = operator.applyAsLong(registerValue, otherValue);
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
