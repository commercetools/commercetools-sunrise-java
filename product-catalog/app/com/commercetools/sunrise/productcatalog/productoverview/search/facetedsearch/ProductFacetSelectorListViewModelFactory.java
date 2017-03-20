package com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch;

import com.commercetools.sunrise.search.facetedsearch.bucketranges.viewmodels.BucketRangeFacetSelectorViewModelFactory;
import com.commercetools.sunrise.search.facetedsearch.sliderranges.viewmodels.SliderRangeFacetSelectorViewModelFactory;
import com.commercetools.sunrise.search.facetedsearch.terms.viewmodels.TermFacetSelectorViewModelFactory;
import com.commercetools.sunrise.search.facetedsearch.viewmodels.AbstractFacetSelectorListViewModelFactory;
import io.sphere.sdk.products.ProductProjection;

import javax.inject.Inject;

public class ProductFacetSelectorListViewModelFactory extends AbstractFacetSelectorListViewModelFactory<ProductProjection> {

    @Inject
    public ProductFacetSelectorListViewModelFactory(final ProductFacetedSearchFormSettingsList settingsList,
                                                    final TermFacetSelectorViewModelFactory termFacetSelectorViewModelFactory,
                                                    final BucketRangeFacetSelectorViewModelFactory bucketRangeFacetSelectorViewModelFactory,
                                                    final SliderRangeFacetSelectorViewModelFactory sliderRangeFacetSelectorViewModelFactory) {
        super(settingsList, termFacetSelectorViewModelFactory, bucketRangeFacetSelectorViewModelFactory, sliderRangeFacetSelectorViewModelFactory);
    }
}
