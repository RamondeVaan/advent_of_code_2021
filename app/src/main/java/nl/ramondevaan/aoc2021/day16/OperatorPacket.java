package nl.ramondevaan.aoc2021.day16;

import java.util.List;
import java.util.stream.Stream;

public class OperatorPacket extends Packet {
    private final int lengthTypeId;
    private final List<Packet> packets;

    public OperatorPacket(int version, int typeId, int lengthTypeId, List<Packet> packets) {
        super(version, typeId);
        this.lengthTypeId = lengthTypeId;
        this.packets = packets;
    }

    @Override
    public long getValue() {
        return lengthTypeId;
    }

    @Override
    public Stream<Packet> subPackets() {
        return this.packets.stream();
    }
}
