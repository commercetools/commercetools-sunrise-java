package com.commercetools.sunrise.search.facetedsearch.viewmodels;

import com.commercetools.sunrise.framework.template.i18n.I18nIdentifierResolver;
import com.commercetools.sunrise.framework.viewmodels.ViewModelFactory;
import com.commercetools.sunrise.search.facetedsearch.ConfiguredFacetedSearchFormSettings;

public abstract class AbstractFacetViewModelFactory<M extends FacetViewModel, T extends ConfiguredFacetedSearchFormSettings, F> extends ViewModelFactory {

    private final I18nIdentifierResolver i18nIdentifierResolver;

    protected AbstractFacetViewModelFactory(final I18nIdentifierResolver i18nIdentifierResolver) {
        this.i18nIdentifierResolver = i18nIdentifierResolver;
    }

    protected final I18nIdentifierResolver getI18nIdentifierResolver() {
        return i18nIdentifierResolver;
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
        viewModel.setLabel(i18nIdentifierResolver.resolveOrKey(settings.getFieldLabel()));
    }

    protected void fillCountHidden(final M viewModel, final T settings, final F facetResult) {
        viewModel.setCountHidden(!settings.isCountDisplayed());
    }

    protected abstract void fillAvailable(final M viewModel, final T settings, final F facetResult);
}
