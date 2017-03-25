package com.commercetools.sunrise.search.facetedsearch.terms.viewmodels;

import com.commercetools.sunrise.search.facetedsearch.terms.ConfiguredTermFacetedSearchFormSettings;
import com.commercetools.sunrise.search.facetedsearch.terms.TermFacetMapperSettings;
import com.commercetools.sunrise.search.facetedsearch.terms.TermFacetedSearchFormSettings;
import com.commercetools.sunrise.search.facetedsearch.terms.TermFacetedSearchFormSettingsFactory;
import com.commercetools.sunrise.search.facetedsearch.viewmodels.FacetOptionViewModel;
import io.sphere.sdk.search.TermFacetResult;
import io.sphere.sdk.search.TermStats;
import org.junit.Test;
import play.mvc.Http;
import play.test.Helpers;
import play.test.WithApplication;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

import static com.commercetools.sunrise.search.facetedsearch.terms.viewmodels.CustomSortedTermFacetViewModelFactory.comparePositions;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class CustomSortedTermFacetViewModelFactoryTest extends WithApplication {

    @Test
    public void sortsOptionsAsGivenList() throws Exception {
        testCustomSortedMapper(
                asList("A", "B", "C"),
                asList("B", "C", "A"),
                sortedOptions -> assertThat(sortedOptions).containsExactly("A", "B", "C"));
    }

    @Test
    public void leavesUnknownOptionsAtTheEnd() throws Exception {
        testCustomSortedMapper(
                asList("A", "B", "C"),
                asList("B", "D", "A", "C", "E"),
                sortedOptions -> assertThat(sortedOptions).containsExactly("A", "B", "C", "D", "E"));
    }

    @Test
    public void onEmptyListKeepsSameOrder() throws Exception {
        testCustomSortedMapper(
                emptyList(),
                asList("B", "D", "A", "C", "E"),
                sortedOptions -> assertThat(sortedOptions).containsExactly("B", "D", "A", "C", "E"));
    }

    @Test
    public void worksWithEmptyFacetOptions() throws Exception {
        testCustomSortedMapper(
                asList("A", "B", "C"),
                emptyList(),
                sortedOptions -> assertThat(sortedOptions).isEmpty());
    }

    @Test
    public void givenUnsortedFacetOptionsAreNotSorted() throws Exception {
        final List<String> unsortedOptions = asList("B", "A", "C");
        testCustomSortedMapper(
                asList("A", "B", "C"),
                unsortedOptions,
                sortedOptions -> {
                    assertThat(sortedOptions).containsExactly("A", "B", "C");
                    assertThat(unsortedOptions).containsExactly("B", "A", "C");
                });
    }

    @Test
    public void comparisonEnsuresSymmetricRelation() throws Exception {
        assertThat(comparePositions(2, 3)).isEqualTo(-comparePositions(3, 2));
        assertThat(comparePositions(-1, 3)).isEqualTo(-comparePositions(3, -1));
        assertThat(comparePositions(3, -1)).isEqualTo(-comparePositions(-1, 3));
        assertThat(comparePositions(-1, -1)).isEqualTo(-comparePositions(-1, -1));
    }

    @Test
    public void comparisonEnsuresTransitiveRelation() throws Exception {
        assertThat(comparePositions(-1, 0)).isGreaterThan(0);
        assertThat(comparePositions(3, 0)).isGreaterThan(0);
        assertThat(comparePositions(-1, 3)).isGreaterThan(0);
    }

    private void testCustomSortedMapper(final List<String> customSortedValues, final List<String> terms, final Consumer<List<String>> test) {
        final TermFacetMapperSettings mapperSettings = TermFacetMapperSettings.of("customSorted", customSortedValues);
        final ConfiguredTermFacetedSearchFormSettings configuration = ConfiguredTermFacetedSearchFormSettings.of("bar", "", "", false, null, false, false, mapperSettings, null, null);
        final TermFacetedSearchFormSettings<?> settings = new TermFacetedSearchFormSettingsFactory(Locale.ENGLISH).create(configuration);
        final List<TermStats> termStats = terms.stream()
                .map(term -> TermStats.of(term, 0L))
                .collect(toList());
        Http.Context.current.set(new Http.Context(Helpers.fakeRequest()));
        final List<FacetOptionViewModel> options = new CustomSortedTermFacetViewModelFactory(Optional::of, new TermFacetOptionViewModelFactory())
                .createOptions(settings, TermFacetResult.of(0L, 0L, 0L, termStats));
        test.accept(options.stream()
                .map(FacetOptionViewModel::getValue)
                .collect(toList()));
    }
}
