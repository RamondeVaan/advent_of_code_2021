package nl.ramondevaan.aoc2021.day02;

public class ForwardCommand implements Command {

    private final int amount;

    public ForwardCommand(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
