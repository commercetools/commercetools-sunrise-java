package com.commercetools.sunrise.categorytree;

import com.commercetools.sunrise.framework.SunriseConfigurationException;
import io.sphere.sdk.models.Base;
import play.Configuration;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

public final class SpecialCategoryConfiguration extends Base {

    private final String externalId;
    private final List<String> filterExpressions;
    private final boolean sale;

    SpecialCategoryConfiguration(final Configuration configuration) {
        this.externalId = Optional.ofNullable(configuration.getString("externalId"))
                .orElseThrow(() -> new SunriseConfigurationException("Required category external ID", "externalId"));
        this.filterExpressions = configuration.getStringList("filterExpressions", emptyList());
        this.sale = configuration.getBoolean("sale", false);
    }

    public String externalId() {
        return externalId;
    }

    public List<String> filterExpressions() {
        return filterExpressions;
    }

    public boolean isSale() {
        return sale;
    }
}
