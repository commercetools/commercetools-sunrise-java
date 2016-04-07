package productcatalog.productoverview.search;

import io.sphere.sdk.search.SearchExpression;
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

public class SortCriteriaTest {

    private static final String KEY = "key";
    private static final List<SortOption> SORT_OPTIONS = asList(
            SortOption.of("foo-asc", "Foo Asc", singletonList("foo asc")),
            SortOption.of("foo-desc", "Foo Desc", singletonList("foo desc")),
            SortOption.of("foobar-asc", "Foo Bar Asc", asList("foo asc", "bar asc")));

    @Test
    public void getsSelectedSort() throws Exception {
        final SortConfig config = SortConfig.of(KEY, SORT_OPTIONS, singletonList("foo-desc"));
        final List<String> selectedValues = asList("foobar-asc", "foo-asc");
        test(config, selectedValues, sortCriteria ->
                Assertions.assertThat(sortCriteria.getSelectedValues()).containsExactly("foobar-asc", "foo-asc"));
    }

    @Test
    public void getsDefaultWhenNoneSelected() throws Exception {
        final SortConfig config = SortConfig.of(KEY, SORT_OPTIONS, singletonList("foo-desc"));
        final List<String> selectedValues = emptyList();
        test(config, selectedValues, sortCriteria ->
                Assertions.assertThat(sortCriteria.getSelectedValues()).containsExactly("foo-desc"));
    }

    @Test
    public void getsDefaultWhenInvalidSelected() throws Exception {
        final SortConfig config = SortConfig.of(KEY, SORT_OPTIONS, singletonList("foo-desc"));
        final List<String> selectedValues = singletonList("invalid");
        test(config, selectedValues, sortCriteria ->
                Assertions.assertThat(sortCriteria.getSelectedValues()).containsExactly("foo-desc"));
    }

    @Test
    public void generatesSortExpressions() throws Exception {
        final SortConfig config = SortConfig.of(KEY, SORT_OPTIONS, singletonList("foo-desc"));
        final List<String> selectedValues = asList("foobar-asc", "foo-desc");
        test(config, selectedValues, sortCriteria ->
                Assertions.assertThat(sortCriteria.getSelectedSortExpressions())
                        .extracting(SearchExpression::expression)
                        .containsExactly("foo asc", "bar asc", "foo desc"));
    }

    private void test(final SortConfig config, final List<String> selectedValues, final Consumer<SortSelector> test) {
        final Map<String, List<String>> queryString = Collections.singletonMap(KEY, selectedValues);
        final SortSelector criteria = SortSelector.of(config, queryString);
        test.accept(criteria);
    }
}
