package com.commercetools.sunrise.search.facetedsearch.terms.viewmodels;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.search.facetedsearch.terms.TermFacetedSearchFormSettings;
import com.commercetools.sunrise.search.facetedsearch.viewmodels.AbstractFacetSelectorViewModelFactory;
import com.commercetools.sunrise.search.facetedsearch.viewmodels.FacetSelectorViewModel;
import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import io.sphere.sdk.search.TermFacetResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@RequestScoped
public class TermFacetSelectorViewModelFactory extends AbstractFacetSelectorViewModelFactory<TermFacetedSearchFormSettings<?>, TermFacetResult> {

    private final static Logger LOGGER = LoggerFactory.getLogger(TermFacetSelectorViewModelFactory.class);

    private final TermFacetViewModelFactory termFacetViewModelFactory;
    private final Injector injector;

    @Inject
    public TermFacetSelectorViewModelFactory(final TermFacetViewModelFactory termFacetViewModelFactory, final Injector injector) {
        this.termFacetViewModelFactory = termFacetViewModelFactory;
        this.injector = injector;
    }

    protected final TermFacetViewModelFactory getTermFacetViewModelFactory() {
        return termFacetViewModelFactory;
    }

    @Override
    public final FacetSelectorViewModel create(final TermFacetedSearchFormSettings<?> settings, final TermFacetResult facetResult) {
        return super.create(settings, facetResult);
    }

    @Override
    protected final void initialize(final FacetSelectorViewModel viewModel, final TermFacetedSearchFormSettings<?> settings, final TermFacetResult facetResult) {
        super.initialize(viewModel, settings, facetResult);
        fillSelectFacet(viewModel, settings, facetResult);
        fillDisplayList(viewModel, settings, facetResult);
    }

    @Override
    protected void fillFacet(final FacetSelectorViewModel viewModel, final TermFacetedSearchFormSettings<?> settings, final TermFacetResult facetResult) {
        final TermFacetViewModelFactory viewModelFactory = findViewModelFactory(settings);
        viewModel.setFacet(viewModelFactory.create(settings, facetResult));
    }

    protected void fillSelectFacet(final FacetSelectorViewModel viewModel, final TermFacetedSearchFormSettings<?> settings, final TermFacetResult facetResult) {
        if (settings.getUIType() != null) {
            viewModel.setSelectFacet(settings.getUIType().equals("list") || settings.getUIType().equals("columnsList"));
        }
    }

    protected void fillDisplayList(final FacetSelectorViewModel viewModel, final TermFacetedSearchFormSettings<?> settings, final TermFacetResult facetResult) {
        if (settings.getUIType() != null) {
            viewModel.setDisplayList(settings.getUIType().equals("list"));
        }
    }

    private TermFacetViewModelFactory findViewModelFactory(final TermFacetedSearchFormSettings<?> settings) {
        if (settings.getMapperSettings() != null) {
            try {
                final Named named = Names.named(settings.getMapperSettings().getName());
                return injector.getInstance(Key.get(TermFacetViewModelFactory.class, named));
            } catch (ConfigurationException e) {
                LOGGER.warn("Could not find term facet mapper with name \"{}\"", settings.getFieldName());
            }
        }
        return termFacetViewModelFactory;
    }
}
