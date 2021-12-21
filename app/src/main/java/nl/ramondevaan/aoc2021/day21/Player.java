package nl.ramondevaan.aoc2021.day21;

public record Player(int position, int score) {
    public Player roll(int value) {
        int newPosition = (position + value) % 10;
        return new Player(newPosition, score + newPosition + 1);
    }
}
