package com.commercetools.sunrise.models.search.facetedsearch.terms.viewmodels;

import com.commercetools.sunrise.core.i18n.I18nResolver;
import com.commercetools.sunrise.core.injection.RequestScoped;
import com.commercetools.sunrise.models.search.facetedsearch.terms.TermFacetedSearchFormSettings;
import com.commercetools.sunrise.models.search.facetedsearch.viewmodels.FacetOptionViewModel;
import io.sphere.sdk.search.TermFacetResult;

import javax.inject.Inject;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * Mapper that sorts the options according to the given list of values.
 * Any value that do not appear in the list of values is placed at the end of the output list.
 */
@RequestScoped
public final class CustomSortedTermFacetViewModelFactory extends TermFacetViewModelFactory {

    @Inject
    public CustomSortedTermFacetViewModelFactory(final I18nResolver i18nResolver,
                                                 final TermFacetOptionViewModelFactory termFacetOptionViewModelFactory) {
        super(i18nResolver, termFacetOptionViewModelFactory);
    }

    @Override
    protected List<FacetOptionViewModel> createOptions(final TermFacetedSearchFormSettings<?> settings, final TermFacetResult facetResult) {
        final List<String> customSortedValues = customSortedValues(settings);
        return super.createOptions(settings, facetResult).stream()
//                .sorted((left, right) -> comparePositions(left.getValue(), right.getValue(), customSortedValues))
                .collect(toList());
    }

    private static List<String> customSortedValues(final TermFacetedSearchFormSettings<?> settings) {
        if (settings.getMapperSettings() != null && settings.getMapperSettings().getValues() != null) {
            return settings.getMapperSettings().getValues();
        } else {
            return emptyList();
        }
    }
}
