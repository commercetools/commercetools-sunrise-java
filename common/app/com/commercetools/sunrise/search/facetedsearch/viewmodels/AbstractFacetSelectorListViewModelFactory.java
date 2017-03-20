package com.commercetools.sunrise.search.facetedsearch.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import com.commercetools.sunrise.search.facetedsearch.FacetedSearchFormSettingsList;
import com.commercetools.sunrise.search.facetedsearch.bucketranges.BucketRangeFacetedSearchFormSettings;
import com.commercetools.sunrise.search.facetedsearch.bucketranges.viewmodels.BucketRangeFacetSelectorViewModelFactory;
import com.commercetools.sunrise.search.facetedsearch.sliderranges.SliderRangeFacetedSearchFormSettings;
import com.commercetools.sunrise.search.facetedsearch.sliderranges.viewmodels.SliderRangeFacetSelectorViewModelFactory;
import com.commercetools.sunrise.search.facetedsearch.terms.TermFacetedSearchFormSettings;
import com.commercetools.sunrise.search.facetedsearch.terms.viewmodels.TermFacetSelectorViewModelFactory;
import io.sphere.sdk.search.PagedSearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractFacetSelectorListViewModelFactory<T> extends SimpleViewModelFactory<FacetSelectorListViewModel, PagedSearchResult<T>> {

    private final FacetedSearchFormSettingsList<T> settingsList;
    private final TermFacetSelectorViewModelFactory termFacetSelectorViewModelFactory;
    private final BucketRangeFacetSelectorViewModelFactory bucketRangeFacetSelectorViewModelFactory;
    private final SliderRangeFacetSelectorViewModelFactory sliderRangeFacetSelectorViewModelFactory;

    protected AbstractFacetSelectorListViewModelFactory(final FacetedSearchFormSettingsList<T> settingsList,
                                                        final TermFacetSelectorViewModelFactory termFacetSelectorViewModelFactory,
                                                        final BucketRangeFacetSelectorViewModelFactory bucketRangeFacetSelectorViewModelFactory,
                                                        final SliderRangeFacetSelectorViewModelFactory sliderRangeFacetSelectorViewModelFactory) {
        this.settingsList = settingsList;
        this.termFacetSelectorViewModelFactory = termFacetSelectorViewModelFactory;
        this.bucketRangeFacetSelectorViewModelFactory = bucketRangeFacetSelectorViewModelFactory;
        this.sliderRangeFacetSelectorViewModelFactory = sliderRangeFacetSelectorViewModelFactory;
    }

    protected final FacetedSearchFormSettingsList<T> getSettingsList() {
        return settingsList;
    }

    protected final TermFacetSelectorViewModelFactory getTermFacetSelectorViewModelFactory() {
        return termFacetSelectorViewModelFactory;
    }

    protected final BucketRangeFacetSelectorViewModelFactory getBucketRangeFacetSelectorViewModelFactory() {
        return bucketRangeFacetSelectorViewModelFactory;
    }

    protected final SliderRangeFacetSelectorViewModelFactory getSliderRangeFacetSelectorViewModelFactory() {
        return sliderRangeFacetSelectorViewModelFactory;
    }

    @Override
    protected final FacetSelectorListViewModel newViewModelInstance(final PagedSearchResult<T> pagedSearchResult) {
        return new FacetSelectorListViewModel();
    }

    @Override
    public final FacetSelectorListViewModel create(final PagedSearchResult<T> pagedSearchResult) {
        final FacetSelectorListViewModel viewModel = newViewModelInstance(pagedSearchResult);
        initialize(viewModel, pagedSearchResult);
        return viewModel;
    }

    @Override
    protected final void initialize(final FacetSelectorListViewModel viewModel, final PagedSearchResult<T> pagedSearchResult) {
        fillList(viewModel, pagedSearchResult);
    }

    protected void fillList(final FacetSelectorListViewModel viewModel, final PagedSearchResult<T> pagedSearchResult) {
        final List<FacetSelectorViewModel> list = new ArrayList<>();
        settingsList.getSettings().forEach(settings -> {
            if (settings instanceof TermFacetedSearchFormSettings) {
                createViewModel((TermFacetedSearchFormSettings<T>) settings, pagedSearchResult).ifPresent(list::add);
            } else if (settings instanceof SliderRangeFacetedSearchFormSettings) {
                createViewModel((SliderRangeFacetedSearchFormSettings<T>) settings, pagedSearchResult).ifPresent(list::add);
            } else if (settings instanceof BucketRangeFacetedSearchFormSettings) {
                createViewModel((BucketRangeFacetedSearchFormSettings<T>) settings, pagedSearchResult).ifPresent(list::add);
            }
        });
        viewModel.setList(list);
    }

    private Optional<FacetSelectorViewModel> createViewModel(final TermFacetedSearchFormSettings<T> settings, final PagedSearchResult<T> pagedSearchResult) {
        return settings.findFacetResult(pagedSearchResult)
                .map(facetResult -> termFacetSelectorViewModelFactory.create(settings, facetResult));
    }

    private Optional<FacetSelectorViewModel> createViewModel(final SliderRangeFacetedSearchFormSettings<T> settings, final PagedSearchResult<T> pagedSearchResult) {
        return settings.findFacetResult(pagedSearchResult)
                .map(facetResult -> sliderRangeFacetSelectorViewModelFactory.create(settings, facetResult));
    }

    private Optional<FacetSelectorViewModel> createViewModel(final BucketRangeFacetedSearchFormSettings<T> settings, final PagedSearchResult<T> pagedSearchResult) {
        return settings.findFacetResult(pagedSearchResult)
                .map(facetResult -> bucketRangeFacetSelectorViewModelFactory.create(settings, facetResult));
    }
}
