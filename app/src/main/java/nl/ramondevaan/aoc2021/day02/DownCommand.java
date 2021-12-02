package nl.ramondevaan.aoc2021.day02;

public class DownCommand implements Command {
    private final int amount;

    public DownCommand(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
