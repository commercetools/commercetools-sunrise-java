package com.commercetools.sunrise.search.facetedsearch.viewmodels;

import com.commercetools.sunrise.framework.i18n.I18nResolver;
import com.commercetools.sunrise.framework.viewmodels.ViewModelFactory;
import com.commercetools.sunrise.search.facetedsearch.ConfiguredFacetedSearchFormSettings;

public abstract class AbstractFacetViewModelFactory<M extends FacetViewModel, T extends ConfiguredFacetedSearchFormSettings, F> extends ViewModelFactory {

    private final I18nResolver i18nResolver;

    protected AbstractFacetViewModelFactory(final I18nResolver i18nResolver) {
        this.i18nResolver = i18nResolver;
    }

    protected final I18nResolver getI18nResolver() {
        return i18nResolver;
    }

    protected abstract M newViewModelInstance(final T settings, final F facetResult);

    public M create(final T settings, final F facetResult) {
        final M viewModel = newViewModelInstance(settings, facetResult);
        initialize(viewModel, settings, facetResult);
        return viewModel;
    }

    protected void initialize(final M viewModel, final T settings, final F facetResult) {
        fillLabel(viewModel, settings, facetResult);
        fillCountHidden(viewModel, settings, facetResult);
        fillAvailable(viewModel, settings, facetResult);
    }

    protected void fillLabel(final M viewModel, final T settings, final F facetResult) {
        viewModel.setLabel(i18nResolver.getOrKey(settings.getFieldLabel()));
    }

    protected void fillCountHidden(final M viewModel, final T settings, final F facetResult) {
        viewModel.setCountHidden(!settings.isCountDisplayed());
    }

    protected abstract void fillAvailable(final M viewModel, final T settings, final F facetResult);
}
