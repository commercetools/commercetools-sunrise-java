package com.commercetools.sunrise.ctp.categories;

import com.commercetools.sunrise.play.configuration.SunriseConfigurationException;
import io.sphere.sdk.models.Base;
import play.Configuration;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

final class SpecialCategorySettingsImpl extends Base implements SpecialCategorySettings {

    private final String externalId;
    private final List<String> productFilterExpressions;
    private final boolean sale;

    SpecialCategorySettingsImpl(final Configuration configuration) {
        this.externalId = Optional.ofNullable(configuration.getString("externalId"))
                .orElseThrow(() -> new SunriseConfigurationException("Required category external ID", "externalId"));
        this.productFilterExpressions = configuration.getStringList("productFilterExpressions", emptyList());
        this.sale = configuration.getBoolean("sale", false);
    }

    @Override
    public String externalId() {
        return externalId;
    }

    @Override
    public List<String> productFilterExpressions() {
        return productFilterExpressions;
    }

    @Override
    public boolean isSale() {
        return sale;
    }
}