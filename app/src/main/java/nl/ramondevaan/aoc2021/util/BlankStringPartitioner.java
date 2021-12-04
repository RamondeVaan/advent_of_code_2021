package nl.ramondevaan.aoc2021.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlankStringPartitioner implements Partitioner<String> {
    @Override
    public List<List<String>> partition(List<String> lines) {

        List<List<String>> ret = new ArrayList<>();
        List<String> current = null;
        boolean lastLineBlank = true;

        for (String line : lines) {
            boolean isBlank = line.isBlank();
            if (isBlank) {
                if (!lastLineBlank) {
                    ret.add(Collections.unmodifiableList(current));
                }
            } else {
                if (lastLineBlank) {
                    current = new ArrayList<>();
                }
                current.add(line);
            }
            lastLineBlank = isBlank;
        }

        if (!lastLineBlank) {
            ret.add(Collections.unmodifiableList(current));
        }

        return Collections.unmodifiableList(ret);
    }
}
