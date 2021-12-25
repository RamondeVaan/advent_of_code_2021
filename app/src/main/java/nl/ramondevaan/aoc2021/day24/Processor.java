package nl.ramondevaan.aoc2021.day24;

import java.util.Arrays;
import java.util.List;
import java.util.function.IntBinaryOperator;

import static nl.ramondevaan.aoc2021.day24.MathInstruction.*;

public class Processor {

    private final Input inputInstruction;
    private final List<Instruction> instructions;
    private final int[] registers;

    public Processor(List<Instruction> instructions, int numberOfRegisters) {
        if (instructions.get(0) instanceof Input inputInstruction) {
            this.inputInstruction = inputInstruction;
        } else {
            throw new IllegalArgumentException();
        }
        this.instructions = instructions.subList(1, instructions.size());
        this.registers = new int[numberOfRegisters];
    }

    public int process(int input, int z) {
        Arrays.fill(registers, 0);
        registers[inputInstruction.register()] = input;
        registers[registers.length - 1] = z;
        for (Instruction instruction : instructions) {
            processInstruction(instruction);
        }
        return registers[registers.length - 1];
    }

    private void processInstruction(Instruction instruction) {
        int registerIndex = instruction.register();
        if (instruction instanceof MathInstruction mathInstruction) {
            int registerValue = registers[registerIndex];
            IntBinaryOperator operator = switch (mathInstruction.operation()) {
                case ADD -> Integer::sum;
                case MULTIPLY -> (left, right) -> left * right;
                case DIVIDE -> (left, right) -> left / right;
                case MODULO -> (left, right) -> left % right;
                case EQUAL -> (left, right) -> left == right ? 1 : 0;
                default -> throw new UnsupportedOperationException();
            };

            int otherValue;
            if (instruction instanceof ConstantInstruction constantInstruction) {
                otherValue = constantInstruction.value();
            } else if (instruction instanceof RegisterInstruction registerInstruction) {
                int otherRegisterIndex = registerInstruction.otherRegister();
                otherValue = registers[otherRegisterIndex];
            } else {
                throw new UnsupportedOperationException();
            }

            registers[registerIndex] = operator.applyAsInt(registerValue, otherValue);
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
