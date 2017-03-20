package com.commercetools.sunrise.productcatalog.productoverview.search.facetedsearch;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.search.facetedsearch.FacetedSearchFormSettings;
import com.commercetools.sunrise.search.facetedsearch.FacetedSearchFormSettingsList;
import io.sphere.sdk.products.ProductProjection;

import javax.inject.Inject;
import java.util.List;

@RequestScoped
public final class ProductFacetedSearchFormSettingsList implements FacetedSearchFormSettingsList<ProductProjection> {

    private final FacetedSearchFormSettingsList<ProductProjection> settingsList;

    @Inject
    public ProductFacetedSearchFormSettingsList(final ConfiguredProductFacetedSearchFormSettingsList configurations,
                                                final ProductFacetedSearchFormSettingsListFactory productFacetedSearchFormSettingsListFactory) {
        this.settingsList = productFacetedSearchFormSettingsListFactory.create(configurations);
    }

    @Override
    public List<? extends FacetedSearchFormSettings<ProductProjection>> getSettings() {
        return settingsList.getSettings();
    }
}
