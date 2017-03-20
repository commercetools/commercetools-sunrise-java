package com.commercetools.sunrise.search.facetedsearch.terms;

import javax.annotation.Nullable;
import java.util.List;

public interface TermFacetMapperSettings {

    String getName();

    @Nullable
    List<String> getValues();

    static TermFacetMapperSettings of(final String name, @Nullable final List<String> values) {
        return new TermFacetMapperSettingsImpl(name, values);
    }
}
