package productcatalog.productoverview.search;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static java.util.Collections.*;

public class ProductsPerPageSelectorTest {

    private static final String KEY = "key";

    @Test
    public void getsSelectedPage() throws Exception {
        final ProductsPerPageConfig config = ProductsPerPageConfig.of(KEY, asList(option(1), option(2), option(3)), 2);
        final List<String> selectedValues = singletonList("3");
        test(config, selectedValues, displaySelector ->
                Assertions.assertThat(displaySelector.getSelectedPageSize()).isEqualTo(3));
    }

    @Test
    public void getsDefaultWhenNoneSelected() throws Exception {
        final ProductsPerPageConfig config = ProductsPerPageConfig.of(KEY, asList(option(1), option(2), option(3)), 2);
        final List<String> selectedValues = emptyList();
        test(config, selectedValues, displaySelector ->
                Assertions.assertThat(displaySelector.getSelectedPageSize()).isEqualTo(2));
    }

    @Test
    public void getsDefaultWhenInvalidSelected() throws Exception {
        final ProductsPerPageConfig config = ProductsPerPageConfig.of(KEY, asList(option(1), option(2), option(3)), 2);
        final List<String> selectedValues = singletonList("5");
        test(config, selectedValues, displaySelector ->
                Assertions.assertThat(displaySelector.getSelectedPageSize()).isEqualTo(2));
    }

    @Test
    public void getsDefaultWhenSelectedAllAndNotEnabled() throws Exception {
        final ProductsPerPageConfig config = ProductsPerPageConfig.of(KEY, asList(option(1), option(2), option(3)), 2);
        final List<String> selectedValues = singletonList("500");
        test(config, selectedValues, displaySelector ->
                Assertions.assertThat(displaySelector.getSelectedPageSize()).isEqualTo(2));
    }

    private void test(final ProductsPerPageConfig config, final List<String> selectedValues, final Consumer<ProductsPerPageSelector> test) {
        final Map<String, List<String>> queryString = singletonMap(config.getKey(), selectedValues);
        final ProductsPerPageSelectorFactory factory = ProductsPerPageSelectorFactory.of(config, queryString);
        test.accept(factory.create());
    }

    private static ProductsPerPageOption option(final int amount) {
        final String value = String.valueOf(amount);
        return ProductsPerPageOption.of(value, value, amount);
    }
}
