package nl.ramondevaan.aoc2021.day16;

import java.util.stream.Stream;

public class LiteralPacket extends Packet {

    private final long literal;

    public LiteralPacket(int version, int typeId, long literal) {
        super(version, typeId);
        this.literal = literal;
    }

    @Override
    public long getValue() {
        return literal;
    }

    @Override
    public Stream<Packet> subPackets() {
        return Stream.empty();
    }
}
