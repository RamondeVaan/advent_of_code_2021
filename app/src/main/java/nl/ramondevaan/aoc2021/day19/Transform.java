package nl.ramondevaan.aoc2021.day19;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public record Transform(Rotation rotation, Translation translation) {

    public Position apply(Position position) {
        return translation.apply(rotation.apply(position));
    }

    public static Transform findTransform(Collection<Position> reference, Collection<Position> template) {
        Translation referenceZeroTranslation = Translation.zeroTranslation(reference);
        Translation templateZeroTranslation = Translation.zeroTranslation(template);
        Set<Position> zeroedReference = reference.stream().map(referenceZeroTranslation::apply)
                .collect(Collectors.toUnmodifiableSet());
        Set<Position> zeroedTemplate = template.stream().map(templateZeroTranslation::apply)
                .collect(Collectors.toUnmodifiableSet());

        Rotation rotation = findRotation(zeroedReference, zeroedTemplate);
        if (rotation == null) {
            return null;
        }
        Set<Position> rotatedTemplate = template.stream().map(rotation::apply).collect(Collectors.toUnmodifiableSet());

        Translation translation = findTranslation(reference, rotatedTemplate);

        return new Transform(rotation, translation);
    }

    private static Rotation findRotation(Set<Position> reference, Set<Position> template) {
        for (Rotation rotation : Rotation.values()) {
            Set<Position> rotatedTemplate = template.stream().map(rotation::apply)
                    .collect(Collectors.toUnmodifiableSet());
            Translation zeroTranslation = Translation.zeroTranslation(rotatedTemplate);
            rotatedTemplate = rotatedTemplate.stream().map(zeroTranslation::apply)
                    .collect(Collectors.toUnmodifiableSet());
            if (rotatedTemplate.equals(reference)) {
                return rotation;
            }
        }
        return null;
    }

    private static Translation findTranslation(Collection<Position> reference, Collection<Position> template) {
        Translation referenceToZeroTranslation = Translation.zeroTranslation(reference);
        Translation templateToZeroTranslation = Translation.zeroTranslation(template);

        return new Translation(
                templateToZeroTranslation.x() - referenceToZeroTranslation.x(),
                templateToZeroTranslation.y() - referenceToZeroTranslation.y(),
                templateToZeroTranslation.z() - referenceToZeroTranslation.z()
        );
    }
}
