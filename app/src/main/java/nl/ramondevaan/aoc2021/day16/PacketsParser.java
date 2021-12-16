package nl.ramondevaan.aoc2021.day16;

import nl.ramondevaan.aoc2021.util.Parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HexFormat;
import java.util.List;
import java.util.stream.IntStream;

public class PacketsParser implements Parser<String, Packet> {

    private int[] stream;
    private int streamOffset;

    @Override
    public Packet parse(String toParse) {
        int overflow = toParse.length() % 8;
        String line = overflow == 0 ? toParse : toParse + "0".repeat(8 - overflow);
        this.stream = IntStream.iterate(0, i -> i < line.length(), i -> i + 8)
                .map(i -> HexFormat.fromHexDigits(line, i, i + 8)).toArray();
        this.streamOffset = 0;

        return parsePacket();
    }

    private Packet parsePacket() {
        int version = get(3);
        int typeId = get(3);
        return typeId == 4 ? parseLiteral(version, typeId) : parseOperator(version, typeId);
    }

    private int get(int len) {
        int startIndex = streamOffset / 32;
        int offset = streamOffset % 32;
        this.streamOffset += len;

        int start = stream[startIndex] << offset;

        int overflow = offset + len - 32;
        if (overflow > 0) {
            return (start >>> (offset - overflow)) | (stream[startIndex + 1] >>> (32 - overflow));
        } else {
            return start >>> (32 - len);
        }
    }

    private Packet parseLiteral(int version, int typeId) {
        long literal = 0;
        while ((get(1) > 0)) {
            literal = (literal | get(4)) << 4;
        }
        literal = literal | get(4);

        return new LiteralPacket(version, typeId, literal);
    }

    private Packet parseOperator(int version, int typeId) {
        int lengthTypeId = get(1);

        List<Packet> packets = new ArrayList<>();
        if (lengthTypeId == 0) {
            int subPacketsBits = get(15);
            int targetOffset = streamOffset + subPacketsBits;

            while (streamOffset < targetOffset) {
                packets.add(parsePacket());
            }
        } else {
            int subPackets = get(11);

            for (int i = 0; i < subPackets; i++) {
                packets.add(parsePacket());
            }
        }

        return new OperatorPacket(version, typeId, lengthTypeId, Collections.unmodifiableList(packets));
    }
}
