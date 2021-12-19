package nl.ramondevaan.aoc2021.day19;

public record Translation(int x, int y, int z) {

    public Beacon apply(Beacon beacon) {
        return new Beacon(beacon.x() + x, beacon.y() + y, beacon.z() + z);
    }

    public static Translation zeroTranslation(Iterable<Beacon> beacons) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;

        for (Beacon beacon : beacons) {
            minX = Math.min(minX, beacon.x());
            minY = Math.min(minY, beacon.y());
            minZ = Math.min(minZ, beacon.z());
        }

        return new Translation(-minX, -minY, -minZ);
    }
}
