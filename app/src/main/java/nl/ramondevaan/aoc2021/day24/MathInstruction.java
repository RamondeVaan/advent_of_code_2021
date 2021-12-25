package nl.ramondevaan.aoc2021.day24;

public interface MathInstruction extends Instruction {
    int ADD = 0;
    int MULTIPLY = 1;
    int DIVIDE = 2;
    int MODULO = 3;
    int EQUAL = 4;

    int operation();
}
