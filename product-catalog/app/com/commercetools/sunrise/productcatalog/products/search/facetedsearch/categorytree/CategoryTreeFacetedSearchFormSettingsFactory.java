package com.commercetools.sunrise.productcatalog.products.search.facetedsearch.categorytree;

import com.commercetools.sunrise.core.SunriseModel;
import com.commercetools.sunrise.models.categories.CategorySettings;
import com.commercetools.sunrise.models.categories.CategoryTreeInCache;
import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Inject;
import java.time.Duration;
import java.util.Locale;

import static io.sphere.sdk.client.SphereClientUtils.blockingWait;

public class CategoryTreeFacetedSearchFormSettingsFactory extends SunriseModel {

    private final Locale locale;
    private final CategorySettings categorySettings;
    private final CategoryTree categoryTree;

    @Inject
    public CategoryTreeFacetedSearchFormSettingsFactory(final Locale locale, final CategorySettings categorySettings,
                                                        final CategoryTreeInCache categoryTreeInCache) {
        this.locale = locale;
        this.categorySettings = categorySettings;
        this.categoryTree = blockingWait(categoryTreeInCache.require(), Duration.ofSeconds(30));
    }

    public CategoryTreeFacetedSearchFormSettings create(final ConfiguredCategoryTreeFacetedSearchFormSettings configuration) {
        return new CategoryTreeFacetedSearchFormSettingsImpl(configuration, locale, categorySettings, categoryTree);
    }
}