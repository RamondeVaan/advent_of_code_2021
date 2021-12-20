package nl.ramondevaan.aoc2021.day18;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nl.ramondevaan.aoc2021.util.CombinatoricsUtils.allPairs;

public class Day18 {

    private final List<List<Element>> numbers;

    public Day18(List<String> lines) {
        NumberParser parser = new NumberParser();
        this.numbers = lines.stream().map(parser::parse).toList();
    }

    public long solve1() {
        return numbers.stream().<List<Element>>map(ArrayList::new).reduce(this::add).map(this::magnitude).orElseThrow();
    }

    private List<Element> add(List<Element> left, List<Element> right) {
        List<Element> add = Stream.concat(left.stream(), right.stream())
                .map(element -> new Element(element.value(), element.depth() + 1))
                .collect(Collectors.toList());
        reduce(add);
        return Collections.unmodifiableList(add);
    }

    private void reduce(List<Element> number) {
        while (true) {
            if (reduceExplosions(number)) {
                continue;
            }
            if (!reduceSplits(number)) {
                break;
            }
        }
    }

    private boolean reduceExplosions(List<Element> number) {
        ListIterator<Element> iterator = number.listIterator();

        while (iterator.hasNext()) {
            Element element = iterator.next();
            if (element.depth() >= 4) {
                iterator.remove();
                if (iterator.hasPrevious()) {
                    Element firstToLeft = iterator.previous();
                    iterator.remove();
                    iterator.add(new Element(
                            firstToLeft.value() + element.value(),
                            firstToLeft.depth()
                    ));
                }
                iterator.add(new Element(0, element.depth() - 1));
                Element right = iterator.next();
                iterator.remove();
                if (iterator.hasNext()) {
                    Element firstToRight = iterator.next();
                    iterator.remove();
                    iterator.add(new Element(
                            firstToRight.value() + right.value(),
                            firstToRight.depth()
                    ));
                }
                return true;
            }
        }

        return false;
    }

    private boolean reduceSplits(List<Element> number) {
        ListIterator<Element> iterator = number.listIterator();

        while (iterator.hasNext()) {
            Element element = iterator.next();

            if (element.value() >= 10) {
                iterator.remove();
                iterator.add(new Element(element.value() / 2, element.depth() + 1));
                iterator.add(new Element(
                        element.value() / 2 + element.value() % 2,
                        element.depth() + 1
                ));

                return true;
            }
        }

        return false;
    }

    private long magnitude(List<Element> number) {
        List<Element> mutableCopy = new ArrayList<>(number);

        for (int depth = 4; depth >= 0; depth--) {
            ListIterator<Element> iterator = mutableCopy.listIterator();
            while (iterator.hasNext()) {
                Element left = iterator.next();
                if (left.depth() == depth) {
                    iterator.remove();
                    Element right = iterator.next();
                    iterator.remove();
                    iterator.add(new Element(3 * left.value() + 2 * right.value(), depth - 1));
                }
            }
        }

        return mutableCopy.get(0).value();
    }

    public long solve2() {
        return allPairs(numbers.size())
                .map(pair -> add(numbers.get(pair.left()), numbers.get(pair.right())))
                .mapToLong(this::magnitude)
                .max().orElseThrow();
    }
}
