package nl.ramondevaan.aoc2021.day18;

import nl.ramondevaan.aoc2021.util.Parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NumberParser implements Parser<String, List<Element>> {
    @Override
    public List<Element> parse(String toParse) {
        char[] chars = toParse.toCharArray();
        List<Element> ret = new ArrayList<>();

        int depth = -1;
        for (char character : chars) {
            if (character == '[') {
                depth++;
            } else if (character == ']') {
                depth--;
            } else if(Character.isDigit(character)) {
                ret.add(new Element(character - '0', depth));
            }
        }

        return Collections.unmodifiableList(ret);
    }
}
