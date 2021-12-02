package nl.ramondevaan.aoc2021.day02;

import nl.ramondevaan.aoc2021.util.Parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandParser implements Parser<String, Command> {

    private final Pattern COMMAND_REGEX = Pattern.compile("(?<command>\\w+) (?<amount>\\d+)");

    @Override
    public Command parse(String toParse) {
        Matcher matcher = COMMAND_REGEX.matcher(toParse);

        if (!matcher.matches()) {
            throw new UnsupportedOperationException();
        }

        String command = matcher.group("command");
        int amount = Integer.parseInt(matcher.group("amount"));

        switch (command) {
            case "forward":
                return new ForwardCommand(amount);
            case "down":
                return new DownCommand(amount);
            case "up":
                return new UpCommand(amount);
        }

        throw new UnsupportedOperationException();
    }
}
