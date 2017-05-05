package com.commercetools.sunrise.search.facetedsearch.terms.viewmodels;

import com.commercetools.sunrise.framework.template.i18n.I18nIdentifierResolver;
import com.commercetools.sunrise.search.facetedsearch.terms.BaseTermFacetedSearchFormSettings;
import com.commercetools.sunrise.search.facetedsearch.viewmodels.AbstractFacetWithOptionsViewModelFactory;
import com.commercetools.sunrise.search.facetedsearch.viewmodels.FacetOptionViewModel;
import io.sphere.sdk.search.TermFacetResult;

import java.util.List;

import static java.util.stream.Collectors.toList;

public abstract class AbstractTermFacetViewModelFactory<S extends BaseTermFacetedSearchFormSettings<?, ?>> extends AbstractFacetWithOptionsViewModelFactory<TermFacetViewModel, S, TermFacetResult> {

    protected AbstractTermFacetViewModelFactory(final I18nIdentifierResolver i18nIdentifierResolver) {
        super(i18nIdentifierResolver);
    }

    @Override
    protected TermFacetViewModel newViewModelInstance(final S settings, final TermFacetResult facetResult) {
        return new TermFacetViewModel();
    }

    @Override
    public final TermFacetViewModel create(final S settings, final TermFacetResult facetResult) {
        return super.create(settings, facetResult);
    }

    @Override
    protected final void initialize(final TermFacetViewModel viewModel, final S settings, final TermFacetResult facetResult) {
        super.initialize(viewModel, settings, facetResult);
        fillKey(viewModel, settings, facetResult);
    }

    protected void fillKey(final TermFacetViewModel viewModel, final S settings, final TermFacetResult facetResult) {
        viewModel.setKey(settings.getFieldName());
    }

    @Override
    protected void fillAvailable(final TermFacetViewModel viewModel, final S settings, final TermFacetResult facetResult) {
        if (settings.getThreshold() != null) {
            viewModel.setAvailable(facetResult.getTerms().size() >= settings.getThreshold());
        } else {
            viewModel.setAvailable(true);
        }
    }

    @Override
    protected void fillLimitedOptions(final TermFacetViewModel viewModel, final S settings, final TermFacetResult facetResult) {
        List<FacetOptionViewModel> options = createOptions(settings, facetResult);
        if (settings.getLimit() != null) {
            options = options.stream()
                    .limit(settings.getLimit())
                    .collect(toList());
        }
        viewModel.setLimitedOptions(options);
    }

    protected abstract List<FacetOptionViewModel> createOptions(final S settings, final TermFacetResult facetResult);
}
