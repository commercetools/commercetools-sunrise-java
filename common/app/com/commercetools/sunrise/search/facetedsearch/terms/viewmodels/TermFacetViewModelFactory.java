package com.commercetools.sunrise.search.facetedsearch.terms.viewmodels;

import com.commercetools.sunrise.framework.i18n.I18nResolver;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.search.facetedsearch.terms.TermFacetedSearchFormSettings;
import com.commercetools.sunrise.search.facetedsearch.viewmodels.FacetOptionViewModel;
import io.sphere.sdk.search.TermFacetResult;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class TermFacetViewModelFactory extends AbstractTermFacetViewModelFactory<TermFacetedSearchFormSettings<?>> {

    private final TermFacetOptionViewModelFactory termFacetOptionViewModelFactory;

    @Inject
    public TermFacetViewModelFactory(final I18nResolver i18nResolver, final TermFacetOptionViewModelFactory termFacetOptionViewModelFactory) {
        super(i18nResolver);
        this.termFacetOptionViewModelFactory = termFacetOptionViewModelFactory;
    }

    protected final TermFacetOptionViewModelFactory getTermFacetOptionViewModelFactory() {
        return termFacetOptionViewModelFactory;
    }

    @Override
    protected List<FacetOptionViewModel> createOptions(final TermFacetedSearchFormSettings<?> settings, final TermFacetResult facetResult) {
        final List<String> selectedValues = settings.getAllSelectedValues(Http.Context.current());
        return facetResult.getTerms().stream()
                .map(stats -> termFacetOptionViewModelFactory.create(stats, selectedValues))
                .collect(toList());
    }
}
