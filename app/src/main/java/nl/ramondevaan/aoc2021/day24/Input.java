package nl.ramondevaan.aoc2021.day24;

public record Input(int register) implements Instruction {
    @Override
    public int register() {
        return register;
    }
}
