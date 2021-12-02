package nl.ramondevaan.aoc2021.day02;

public interface CommandHandler {
    Position handle(Position position, Command command);
}
