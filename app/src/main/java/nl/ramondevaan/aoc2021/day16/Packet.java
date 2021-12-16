package nl.ramondevaan.aoc2021.day16;

import java.util.stream.Stream;

public abstract class Packet {
    private final int version;
    private final int typeId;

    public Packet(int version, int typeId) {
        this.version = version;
        this.typeId = typeId;
    }

    public int getTypeId() {
        return typeId;
    }

    public int getVersion() {
        return version;
    }

    public abstract long getValue();

    public abstract Stream<Packet> subPackets();
}
