package nl.ramondevaan.aoc2021.day24;

public record RegisterInstruction(int operation, int register, int otherRegister) implements MathInstruction {

}
