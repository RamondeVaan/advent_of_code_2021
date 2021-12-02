package nl.ramondevaan.aoc2021.day02;

import java.util.List;

public class Day02 {
    private final List<Command> commands;
    private final Position startPosition;

    public Day02(List<String> lines) {
        CommandParser commandParser = new CommandParser();

        this.commands = lines.stream().map(commandParser::parse).toList();
        this.startPosition = new Position(0, 0, 0);
    }

    public long solve1() {
        return solve(new PositionalCommandHandler());
    }

    public long solve2() {
        return solve(new AimCommandHandler());
    }

    public long solve(CommandHandler commandHandler) {
        Position currentPosition = startPosition;

        for (Command command : commands) {
            currentPosition = commandHandler.handle(currentPosition, command);
        }

        return (long) currentPosition.getHorizontal() * currentPosition.getDepth();
    }
}
