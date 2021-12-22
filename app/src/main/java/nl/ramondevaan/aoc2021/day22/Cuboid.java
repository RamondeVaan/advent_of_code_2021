package nl.ramondevaan.aoc2021.day22;

import java.util.Optional;
import java.util.stream.Stream;

public record Cuboid(Range xRange, Range yRange, Range zRange) {

    public boolean containedIn(Cuboid cuboid) {
        return xRange.containedIn(cuboid.xRange) && yRange.containedIn(cuboid.yRange) && zRange.containedIn(cuboid.zRange);
    }

    public Stream<Position> stream() {
        return xRange.stream().boxed().flatMap(x -> yRange.stream().boxed()
                .flatMap(y -> zRange.stream().boxed()
                        .map(z -> new Position(x, y, z))));
    }

    public long size() {
        return (long) xRange.size() * yRange.size() * zRange.size();
    }

    public Stream<Cuboid> without(Cuboid cuboid) {
        Stream<Cuboid> stream = xRange.without(cuboid.xRange).map(range -> new Cuboid(range, yRange, zRange));
        stream = Stream.concat(stream, yRange.without(cuboid.yRange)
                .map(range -> new Cuboid(cuboid.xRange, range, zRange)));
        return Stream.concat(stream, zRange.without(cuboid.zRange)
                .map(range -> new Cuboid(cuboid.xRange, cuboid.yRange, range)));
    }

    public Optional<Cuboid> overlap(Cuboid cuboid) {
        Optional<Range> xOverlap = xRange.overlap(cuboid.xRange);
        Optional<Range> yOverlap = yRange.overlap(cuboid.yRange);
        Optional<Range> zOverlap = zRange.overlap(cuboid.zRange);

        if (xOverlap.isEmpty() || yOverlap.isEmpty() || zOverlap.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new Cuboid(xOverlap.get(), yOverlap.get(), zOverlap.get()));
    }
}
