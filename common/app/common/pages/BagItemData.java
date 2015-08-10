package common.pages;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class BagItemData {

    private final int numItems;

    public BagItemData(final int numItems) {
        this.numItems = numItems;
    }

    public List<SelectableData> getBagItems() {
        final Stream<SelectableData> defaultItemStream = Stream.of(bagItemDefaultItem());
        final Stream<SelectableData> bla = IntStream.range(2, numItems).mapToObj(this::bagItemToSelectableItem);

        return Stream.concat(defaultItemStream, bla).collect(toList());
    }

    private SelectableData bagItemDefaultItem() {
        return new SelectableData("1", "", "", "", true);
    }

    private SelectableData bagItemToSelectableItem(final int number) {
        return new SelectableData(Integer.toString(number), "", "", "", false);
    }
}
