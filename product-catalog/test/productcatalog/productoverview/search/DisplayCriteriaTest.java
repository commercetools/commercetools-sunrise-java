package productcatalog.productoverview.search;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class DisplayCriteriaTest {

    private static final String KEY = "key";

    @Test
    public void getsSelectedPage() throws Exception {
        final DisplayConfig config = DisplayConfig.of(KEY, asList(1, 2, 3), 2, false);
        final List<String> selectedValues = singletonList("3");
        test(config, selectedValues, displayCriteria ->
                Assertions.assertThat(displayCriteria.getSelectedPageSize()).isEqualTo(3));
    }

    @Test
    public void getsDefaultWhenNoneSelected() throws Exception {
        final DisplayConfig config = DisplayConfig.of(KEY, asList(1, 2, 3), 2, false);
        final List<String> selectedValues = emptyList();
        test(config, selectedValues, displayCriteria ->
                Assertions.assertThat(displayCriteria.getSelectedPageSize()).isEqualTo(2));
    }

    @Test
    public void getsDefaultWhenInvalidSelected() throws Exception {
        final DisplayConfig config = DisplayConfig.of(KEY, asList(1, 2, 3), 2, false);
        final List<String> selectedValues = singletonList("5");
        test(config, selectedValues, displayCriteria ->
                Assertions.assertThat(displayCriteria.getSelectedPageSize()).isEqualTo(2));
    }

    @Test
    public void getsAllPages() throws Exception {
        final DisplayConfig config = DisplayConfig.of(KEY, asList(1, 2, 3), 2, true);
        final List<String> selectedValues = singletonList("500");
        test(config, selectedValues, displayCriteria ->
                Assertions.assertThat(displayCriteria.getSelectedPageSize()).isEqualTo(500));
    }

    @Test
    public void getsDefaultWhenSelectedAllAndNotEnabled() throws Exception {
        final DisplayConfig config = DisplayConfig.of(KEY, asList(1, 2, 3), 2, false);
        final List<String> selectedValues = singletonList("500");
        test(config, selectedValues, displayCriteria ->
                Assertions.assertThat(displayCriteria.getSelectedPageSize()).isEqualTo(2));
    }

    private void test(final DisplayConfig config, final List<String> selectedValues, final Consumer<DisplayCriteria> test) {
        final Map<String, List<String>> queryString = Collections.singletonMap(KEY, selectedValues);
        final DisplayCriteria criteria = DisplayCriteria.of(config, queryString);
        test.accept(criteria);
    }
}
