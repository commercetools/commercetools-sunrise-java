package com.commercetools.sunrise.search.facetedsearch.terms.viewmodels;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.template.i18n.I18nIdentifierResolver;
import com.commercetools.sunrise.search.facetedsearch.terms.TermFacetedSearchFormSettings;
import com.commercetools.sunrise.search.facetedsearch.terms.viewmodels.TermFacetOptionViewModelFactory;
import com.commercetools.sunrise.search.facetedsearch.terms.viewmodels.TermFacetViewModelFactory;
import com.commercetools.sunrise.search.facetedsearch.viewmodels.FacetOptionViewModel;
import io.sphere.sdk.search.TermFacetResult;
import play.mvc.Http;

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
    public AlphabeticallySortedTermFacetViewModelFactory(final I18nIdentifierResolver i18nIdentifierResolver, final Http.Context httpContext,
                                                         final TermFacetOptionViewModelFactory termFacetOptionViewModelFactory) {
        super(i18nIdentifierResolver, httpContext, termFacetOptionViewModelFactory);
    }

    @Override
    protected List<FacetOptionViewModel> createOptions(final TermFacetedSearchFormSettings<?> settings, final TermFacetResult facetResult) {
        return super.createOptions(settings, facetResult).stream()
                .sorted(Comparator.comparing(FacetOptionViewModel::getLabel))
                .collect(toList());
    }
}
