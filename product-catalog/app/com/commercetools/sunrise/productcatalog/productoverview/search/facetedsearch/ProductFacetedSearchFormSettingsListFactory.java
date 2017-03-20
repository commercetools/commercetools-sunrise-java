package com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch;

import com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch.categorytree.CategoryTreeFacetedSearchFormSettingsFactory;
import com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch.categorytree.ConfiguredCategoryTreeFacetedSearchFormSettings;
import com.commercetools.sunrise.search.facetedsearch.AbstractFacetedSearchFormSettingsListFactory;
import com.commercetools.sunrise.search.facetedsearch.ConfiguredFacetedSearchFormSettings;
import com.commercetools.sunrise.search.facetedsearch.FacetedSearchFormSettings;
import com.commercetools.sunrise.search.facetedsearch.bucketranges.BucketRangeFacetedSearchFormSettingsFactory;
import com.commercetools.sunrise.search.facetedsearch.sliderranges.SliderRangeFacetedSearchFormSettingsFactory;
import com.commercetools.sunrise.search.facetedsearch.terms.TermFacetedSearchFormSettingsFactory;
import io.sphere.sdk.products.ProductProjection;

import javax.inject.Inject;
import java.util.List;

public class ProductFacetedSearchFormSettingsListFactory extends AbstractFacetedSearchFormSettingsListFactory<ProductProjection> {

    private final CategoryTreeFacetedSearchFormSettingsFactory categoryTreeFacetedSearchFormSettingsFactory;

    @Inject
    public ProductFacetedSearchFormSettingsListFactory(final TermFacetedSearchFormSettingsFactory termFacetedSearchFormSettingsFactory,
                                                       final SliderRangeFacetedSearchFormSettingsFactory sliderRangeFacetedSearchFormSettingsFactory,
                                                       final BucketRangeFacetedSearchFormSettingsFactory bucketRangeFacetedSearchFormSettingsFactory,
                                                       final CategoryTreeFacetedSearchFormSettingsFactory categoryTreeFacetedSearchFormSettingsFactory) {
        super(termFacetedSearchFormSettingsFactory, sliderRangeFacetedSearchFormSettingsFactory, bucketRangeFacetedSearchFormSettingsFactory);
        this.categoryTreeFacetedSearchFormSettingsFactory = categoryTreeFacetedSearchFormSettingsFactory;
    }

    @Override
    protected void addSettingsToList(final List<FacetedSearchFormSettings<ProductProjection>> list, final ConfiguredFacetedSearchFormSettings settings) {
        if (settings instanceof ConfiguredCategoryTreeFacetedSearchFormSettings) {
            list.add(categoryTreeFacetedSearchFormSettingsFactory.create((ConfiguredCategoryTreeFacetedSearchFormSettings) settings));
        } else {
            super.addSettingsToList(list, settings);
        }
    }
}
