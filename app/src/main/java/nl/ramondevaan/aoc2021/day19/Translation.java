package nl.ramondevaan.aoc2021.day19;

public record Translation(int x, int y, int z) {

    public Position apply(Position position) {
        return new Position(position.x() + x, position.y() + y, position.z() + z);
    }

    public static Translation zeroTranslation(Iterable<Position> beacons) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;

        for (Position position : beacons) {
            minX = Math.min(minX, position.x());
            minY = Math.min(minY, position.y());
            minZ = Math.min(minZ, position.z());
        }

        return new Translation(-minX, -minY, -minZ);
    }
}
