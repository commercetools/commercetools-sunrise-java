package com.commercetools.sunrise.search.facetedsearch.terms;

import com.commercetools.sunrise.framework.SunriseModel;

import javax.annotation.Nullable;
import java.util.List;

final class TermFacetMapperSettingsImpl extends SunriseModel implements TermFacetMapperSettings {

    private final String name;
    @Nullable
    private final List<String> values;

    TermFacetMapperSettingsImpl(final String name, @Nullable final List<String> values) {
        this.name = name;
        this.values = values;
    }

    @Override
    public String getName() {
        return name;
    }

    @Nullable
    @Override
    public List<String> getValues() {
        return values;
    }
}
