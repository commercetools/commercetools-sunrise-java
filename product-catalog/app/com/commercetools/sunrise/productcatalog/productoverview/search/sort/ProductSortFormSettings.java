package com.commercetools.sunrise.productcatalog.productoverview.search.sort;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.search.sort.ConfiguredSortFormSettings;
import com.commercetools.sunrise.search.sort.SortFormOption;
import com.commercetools.sunrise.search.sort.SortFormSettings;
import io.sphere.sdk.products.ProductProjection;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;

@RequestScoped
public final class ProductSortFormSettings implements SortFormSettings<ProductProjection> {

    private final SortFormSettings<ProductProjection> settings;

    @Inject
    public ProductSortFormSettings(final ConfiguredProductSortFormSettings configuration, final Locale locale) {
        this.settings = SortFormSettings.of(configuration, locale);
    }

    @Override
    public List<SortFormOption> getOptions() {
        return settings.getOptions();
    }

    @Override
    public ConfiguredSortFormSettings configuration() {
        return settings.configuration();
    }
}
