package common.pages;

import common.models.BagItemDataFactory;
import common.models.SelectableData;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class BagItemDataFactoryTest {

    @Test
    public void create() {
        final int size = 20;

        final List<SelectableData> bagItems = BagItemDataFactory.of().create(size).getBagItems();
        final List<SelectableData> bagItemsWithoutDefault = getBagItemsWithoutDefault(bagItems);

        assertThat(bagItems).hasSize(size);
        assertThat(bagItems.get(0).isSelected()).isEqualTo(true);
        assertThat(bagItemsWithoutDefault.stream().map(SelectableData::isSelected).map(Boolean::new).collect(toList()))
                .containsOnlyElementsOf(singletonList(Boolean.FALSE));
        assertThat(bagItems.stream().map(SelectableData::getText).collect(toList()))
                .isEqualTo(IntStream.range(1, 21).mapToObj(Integer::toString).collect(toList()));
    }

    private List<SelectableData> getBagItemsWithoutDefault(final List<SelectableData> bagItems) {
        final List<SelectableData> bagItemsWithoutDefault = new ArrayList<>(bagItems);
        bagItemsWithoutDefault.remove(0);
        return bagItemsWithoutDefault;
    }
}
