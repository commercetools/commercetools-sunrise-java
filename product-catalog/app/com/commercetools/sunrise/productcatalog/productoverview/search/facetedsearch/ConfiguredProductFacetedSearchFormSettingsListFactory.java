package com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch;

import com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch.categorytree.ConfiguredCategoryTreeFacetedSearchFormSettingsFactory;
import com.commercetools.sunrise.search.facetedsearch.AbstractConfiguredFacetedSearchFormSettingsListFactory;
import com.commercetools.sunrise.search.facetedsearch.ConfiguredFacetedSearchFormSettings;
import com.commercetools.sunrise.search.facetedsearch.bucketranges.ConfiguredBucketRangeFacetedSearchFormSettingsFactory;
import com.commercetools.sunrise.search.facetedsearch.sliderranges.ConfiguredSliderRangeFacetedSearchFormSettingsFactory;
import com.commercetools.sunrise.search.facetedsearch.terms.ConfiguredTermFacetedSearchFormSettingsFactory;
import play.Configuration;

import javax.inject.Inject;
import java.util.List;

public class ConfiguredProductFacetedSearchFormSettingsListFactory extends AbstractConfiguredFacetedSearchFormSettingsListFactory {

    private static final String CONFIG_TYPE_CATEGORY_TREE = "categoryTree";

    private final ConfiguredCategoryTreeFacetedSearchFormSettingsFactory configuredCategoryTreeFacetedSearchFormSettingsFactory;

    @Inject
    public ConfiguredProductFacetedSearchFormSettingsListFactory(final ConfiguredTermFacetedSearchFormSettingsFactory configuredTermFacetedSearchFormSettingsFactory,
                                                                 final ConfiguredSliderRangeFacetedSearchFormSettingsFactory configuredSliderRangeFacetedSearchFormSettingsFactory,
                                                                 final ConfiguredBucketRangeFacetedSearchFormSettingsFactory configuredBucketRangeFacetedSearchFormSettingsFactory,
                                                                 final ConfiguredCategoryTreeFacetedSearchFormSettingsFactory configuredCategoryTreeFacetedSearchFormSettingsFactory) {
        super(configuredTermFacetedSearchFormSettingsFactory, configuredSliderRangeFacetedSearchFormSettingsFactory, configuredBucketRangeFacetedSearchFormSettingsFactory);
        this.configuredCategoryTreeFacetedSearchFormSettingsFactory = configuredCategoryTreeFacetedSearchFormSettingsFactory;
    }

    @Override
    protected void addSettingsToList(final List<ConfiguredFacetedSearchFormSettings> list, final Configuration configuration, final String type) {
        if (type.equals(CONFIG_TYPE_CATEGORY_TREE)) {
            list.add(configuredCategoryTreeFacetedSearchFormSettingsFactory.create(configuration));
        } else {
            super.addSettingsToList(list, configuration, type);
        }
    }
}
