package nl.ramondevaan.aoc2021.day02;

public class UpCommand implements Command {

    private final int amount;

    public UpCommand(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
