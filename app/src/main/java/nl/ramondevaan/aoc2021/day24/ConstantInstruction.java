package nl.ramondevaan.aoc2021.day24;

public record ConstantInstruction(int operation, int register, int value) implements MathInstruction {

}
