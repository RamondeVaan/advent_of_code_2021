package nl.ramondevaan.aoc2021.day19;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public record Transform(Rotation rotation, Translation translation) {

    public Beacon apply(Beacon beacon) {
        return translation.apply(rotation.apply(beacon));
    }

    public static Transform findTransform(Collection<Beacon> reference, Collection<Beacon> template) {
        Translation referenceZeroTranslation = Translation.zeroTranslation(reference);
        Translation templateZeroTranslation = Translation.zeroTranslation(template);
        Set<Beacon> zeroedReference = reference.stream().map(referenceZeroTranslation::apply)
                .collect(Collectors.toUnmodifiableSet());
        Set<Beacon> zeroedTemplate = template.stream().map(templateZeroTranslation::apply)
                .collect(Collectors.toUnmodifiableSet());

        Rotation rotation = findRotation(zeroedReference, zeroedTemplate);
        if (rotation == null) {
            return null;
        }
        Set<Beacon> rotatedTemplate = template.stream().map(rotation::apply).collect(Collectors.toUnmodifiableSet());

        Translation translation = findTranslation(reference, rotatedTemplate);

        return new Transform(rotation, translation);
    }

    private static Rotation findRotation(Set<Beacon> reference, Set<Beacon> template) {
        for (Rotation rotation : Rotation.values()) {
            Set<Beacon> rotatedTemplate = template.stream().map(rotation::apply)
                    .collect(Collectors.toUnmodifiableSet());
            Translation zeroTranslation = Translation.zeroTranslation(rotatedTemplate);
            rotatedTemplate = rotatedTemplate.stream().map(zeroTranslation::apply)
                    .collect(Collectors.toUnmodifiableSet());
            if (rotatedTemplate.equals(reference)) {
                return rotation;
            }
        }
        throw null;
    }

    private static Translation findTranslation(Collection<Beacon> reference, Collection<Beacon> template) {
        Translation referenceToZeroTranslation = Translation.zeroTranslation(reference);
        Translation templateToZeroTranslation = Translation.zeroTranslation(template);

        return new Translation(
                templateToZeroTranslation.x() - referenceToZeroTranslation.x(),
                templateToZeroTranslation.y() - referenceToZeroTranslation.y(),
                templateToZeroTranslation.z() - referenceToZeroTranslation.z()
        );
    }
}
