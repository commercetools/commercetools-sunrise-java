package com.commercetools.sunrise.models.search.facetedsearch.terms.viewmodels;

import com.commercetools.sunrise.core.i18n.I18nResolver;
import com.commercetools.sunrise.core.injection.RequestScoped;
import com.commercetools.sunrise.models.search.facetedsearch.terms.TermFacetedSearchFormSettings;
import com.commercetools.sunrise.models.search.facetedsearch.viewmodels.FacetOptionViewModel;
import io.sphere.sdk.search.TermFacetResult;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Mapper that sorts the options according to the alphabetical order of the label.
 */
@RequestScoped
public final class AlphabeticallySortedTermFacetViewModelFactory extends TermFacetViewModelFactory {

    @Inject
    public AlphabeticallySortedTermFacetViewModelFactory(final I18nResolver i18nResolver,
                                                         final TermFacetOptionViewModelFactory termFacetOptionViewModelFactory) {
        super(i18nResolver, termFacetOptionViewModelFactory);
    }

    @Override
    protected List<FacetOptionViewModel> createOptions(final TermFacetedSearchFormSettings<?> settings, final TermFacetResult facetResult) {
        return super.createOptions(settings, facetResult).stream()
                .sorted(Comparator.comparing(FacetOptionViewModel::getLabel))
                .collect(toList());
    }
}
