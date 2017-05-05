package com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch;

import com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch.categorytree.CategoryTreeFacetedSearchFormSettings;
import com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch.categorytree.viewmodels.CategoryTreeFacetSelectorViewModelFactory;
import com.commercetools.sunrise.search.facetedsearch.FacetedSearchFormSettings;
import com.commercetools.sunrise.search.facetedsearch.bucketranges.viewmodels.BucketRangeFacetSelectorViewModelFactory;
import com.commercetools.sunrise.search.facetedsearch.sliderranges.viewmodels.SliderRangeFacetSelectorViewModelFactory;
import com.commercetools.sunrise.search.facetedsearch.terms.viewmodels.TermFacetSelectorViewModelFactory;
import com.commercetools.sunrise.search.facetedsearch.viewmodels.AbstractFacetSelectorListViewModelFactory;
import com.commercetools.sunrise.search.facetedsearch.viewmodels.FacetSelectorViewModel;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.PagedSearchResult;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class ProductFacetSelectorListViewModelFactory extends AbstractFacetSelectorListViewModelFactory<ProductProjection> {

    private final CategoryTreeFacetSelectorViewModelFactory categoryTreeFacetSelectorViewModelFactory;

    @Inject
    public ProductFacetSelectorListViewModelFactory(final ProductFacetedSearchFormSettingsList settingsList,
                                                    final TermFacetSelectorViewModelFactory termFacetSelectorViewModelFactory,
                                                    final BucketRangeFacetSelectorViewModelFactory bucketRangeFacetSelectorViewModelFactory,
                                                    final SliderRangeFacetSelectorViewModelFactory sliderRangeFacetSelectorViewModelFactory,
                                                    final CategoryTreeFacetSelectorViewModelFactory categoryTreeFacetSelectorViewModelFactory) {
        super(settingsList, termFacetSelectorViewModelFactory, bucketRangeFacetSelectorViewModelFactory, sliderRangeFacetSelectorViewModelFactory);
        this.categoryTreeFacetSelectorViewModelFactory = categoryTreeFacetSelectorViewModelFactory;
    }

    @Override
    protected void addViewModelToList(final List<FacetSelectorViewModel> list, final FacetedSearchFormSettings<ProductProjection> settings, final PagedSearchResult<ProductProjection> pagedSearchResult) {
        if (settings instanceof CategoryTreeFacetedSearchFormSettings) {
            createViewModel((CategoryTreeFacetedSearchFormSettings) settings, pagedSearchResult).ifPresent(list::add);
        } else {
            super.addViewModelToList(list, settings, pagedSearchResult);
        }
    }

    private Optional<FacetSelectorViewModel> createViewModel(final CategoryTreeFacetedSearchFormSettings settings, final PagedSearchResult<ProductProjection> pagedSearchResult) {
        return settings.findFacetResult(pagedSearchResult)
                .map(facetResult -> categoryTreeFacetSelectorViewModelFactory.create(settings, facetResult));
    }
}
