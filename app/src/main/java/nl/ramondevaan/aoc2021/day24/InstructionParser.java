package nl.ramondevaan.aoc2021.day24;

import nl.ramondevaan.aoc2021.util.Parser;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static nl.ramondevaan.aoc2021.day24.MathInstruction.*;

public class InstructionParser implements Parser<List<String>, List<Instruction>> {

    private final static Pattern INPUT_PATTERN = Pattern.compile("inp (?<register>[a-z])");
    private final static Pattern REGISTER_PATTERN = Pattern.compile(
            "(?<operation>add|mul|div|mod|eql) (?<from>[a-z]) (?<to>[a-z])");
    private final static Pattern CONSTANT_PATTERN = Pattern.compile(
            "(?<operation>add|mul|div|mod|eql) (?<register>[a-z]) (?<value>-?\\d+)");

    @Override
    public List<Instruction> parse(List<String> toParse) {
        return toParse.stream().map(line -> {
                    Matcher matcher = INPUT_PATTERN.matcher(line);
                    if (matcher.matches()) {
                        return parseInput(matcher);
                    }
                    matcher = REGISTER_PATTERN.matcher(line);
                    if (matcher.matches()) {
                        return parseRegisterInstruction(matcher);
                    }
                    matcher = CONSTANT_PATTERN.matcher(line);
                    if (matcher.matches()) {
                        return parseConstantInstruction(matcher);
                    }

                    throw new UnsupportedOperationException();
                })
                .toList();
    }

    private static Instruction parseInput(Matcher matcher) {
        return new Input(getRegisterIndex(matcher.group("register")));
    }

    private static Instruction parseRegisterInstruction(Matcher matcher) {
        return new RegisterInstruction(
                getOperation(matcher.group("operation")),
                getRegisterIndex(matcher.group("from")),
                getRegisterIndex(matcher.group("to"))
        );
    }

    private static Instruction parseConstantInstruction(Matcher matcher) {
        return new ConstantInstruction(
                getOperation(matcher.group("operation")),
                getRegisterIndex(matcher.group("register")),
                Integer.parseInt(matcher.group("value"))
        );
    }

    private static int getOperation(String type) {
        return switch (type) {
            case "add" -> ADD;
            case "mul" -> MULTIPLY;
            case "div" -> DIVIDE;
            case "mod" -> MODULO;
            case "eql" -> EQUAL;
            default -> throw new UnsupportedOperationException();
        };
    }

    private static int getRegisterIndex(String register) {
        return register.charAt(0) - 'w';
    }
}
