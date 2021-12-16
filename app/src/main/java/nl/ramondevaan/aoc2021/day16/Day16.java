package nl.ramondevaan.aoc2021.day16;

import nl.ramondevaan.aoc2021.util.LongBiPredicate;

import java.util.List;
import java.util.function.LongBinaryOperator;
import java.util.stream.LongStream;

public class Day16 {

    private final Packet packet;

    public Day16(List<String> lines) {
        PacketsParser parser = new PacketsParser();
        this.packet = parser.parse(String.join("", lines));
    }

    public long solve1() {
        return sumVersion(packet);
    }

    private long sumVersion(Packet packet) {
        return packet.getVersion() + packet.subPackets().mapToLong(this::sumVersion).sum();
    }

    public long solve2() {
        return decode(packet);
    }

    private long decode(Packet packet) {
        LongStream valueStream = packet.subPackets().mapToLong(this::decode);
        return switch (packet.getTypeId()) {
            case 0 -> valueStream.sum();
            case 1 -> valueStream.reduce(1L, (left, right) -> left * right);
            case 2 -> valueStream.min().orElseThrow();
            case 3 -> valueStream.max().orElseThrow();
            case 4 -> packet.getValue();
            case 5 -> valueStream.reduce(byPredicate((left, right) -> left > right)).orElseThrow();
            case 6 -> valueStream.reduce(byPredicate((left, right) -> left < right)).orElseThrow();
            case 7 -> valueStream.reduce(byPredicate((left, right) -> left == right)).orElseThrow();
            default -> throw new IllegalStateException();
        };
    }

    private LongBinaryOperator byPredicate(LongBiPredicate predicate) {
        return (left, right) -> predicate.test(left, right) ? 1L : 0L;
    }
}
